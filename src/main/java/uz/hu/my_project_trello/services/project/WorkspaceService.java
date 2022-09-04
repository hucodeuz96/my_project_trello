package uz.hu.my_project_trello.services.project;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.hu.my_project_trello.domains.auth.AuthRole;
import uz.hu.my_project_trello.domains.auth.AuthUser;
import uz.hu.my_project_trello.domains.project.AbsDomain;
import uz.hu.my_project_trello.domains.project.Board;
import uz.hu.my_project_trello.domains.project.Workspace;
import uz.hu.my_project_trello.dtos.project.AddMemberDTO;
import uz.hu.my_project_trello.dtos.project.board.BoardResDTO;
import uz.hu.my_project_trello.dtos.project.workspace.WorkspaceCreateDTO;
import uz.hu.my_project_trello.dtos.project.workspace.WorkspaceResDTO;
import uz.hu.my_project_trello.dtos.project.workspace.WorkspaceResForBoardMember;
import uz.hu.my_project_trello.dtos.project.workspace.WorkspaceUpdateDTO;
import uz.hu.my_project_trello.exceptions.GenericNotFoundException;
import uz.hu.my_project_trello.mappers.BoardMapper;
import uz.hu.my_project_trello.mappers.WorkspaceMapper;
import uz.hu.my_project_trello.repository.AuthUserRepository;
import uz.hu.my_project_trello.repository.BoardRepository;
import uz.hu.my_project_trello.repository.RoleRepository;
import uz.hu.my_project_trello.repository.WorkspaceRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author "Husniddin Ulachov"
 * @created 2:25 PM on 8/26/2022
 * @project my_project_trello
 */
@RequiredArgsConstructor
@Service
public class WorkspaceService extends AbsProjectService<WorkspaceResDTO, WorkspaceUpdateDTO, WorkspaceCreateDTO,Long>{
    private final WorkspaceMapper workspaceMapper;
    private final WorkspaceRepository workspaceRepository;
    private final AuthUserRepository authUserRepository;
    private final RoleRepository roleRepository;
    private final BoardMapper boardMapper;
    private final BoardRepository boardRepository;
    public  Long getSessionId(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        AuthUser authUser = authUserRepository.findByUsername(name).orElseThrow(() -> new GenericNotFoundException("not Authorization", 403));
        return authUser.getId();
    }
    @Override
    public WorkspaceResDTO generate(WorkspaceCreateDTO workspaceCreateDTO) {
        workspaceCreateDTO.setWorkspaceType(workspaceCreateDTO.getWorkspaceType().toUpperCase());
        Workspace workspace = workspaceMapper.fromDTO(workspaceCreateDTO);
        workspace.setCreatedBy(getSessionId());
        if (Objects.nonNull(workspaceCreateDTO.getUserList())){
            ArrayList<AuthUser> list = new ArrayList<>();
            workspaceCreateDTO.getUserList().forEach(id -> {
            AuthUser authUser = authUserRepository.findById(id).orElseThrow(() -> new GenericNotFoundException("not Found", 404));
            list.add(authUser);
             } );
            AuthUser authUser = authUserRepository.findById(getSessionId()).orElseThrow(() -> new GenericNotFoundException("not Found", 404));
            list.add(authUser);
            workspace.setUser(list);
        }
        return workspaceMapper.toDTO(workspaceRepository.save(workspace));
    }
    @Override
    public WorkspaceResDTO edit(WorkspaceUpdateDTO workspaceUpdateDTO) {
        Workspace found = workspaceRepository.findById(workspaceUpdateDTO.getId()).orElseThrow(() -> new GenericNotFoundException("Workspace not found", 404));
        Workspace workspace = workspaceMapper.fromUDTO(workspaceUpdateDTO, found);
        workspace.setUpdatedAt(LocalDateTime.now());
        workspace.setUpdatedBy(getSessionId());
        Workspace workspace1 = workspaceRepository.save(workspace);
        return workspaceMapper.toDTO(workspace1);
    }
    @Override
    public WorkspaceResDTO getOne(Long id) {
        Workspace workspace = workspaceRepository.findOne(getSessionId(), id);
        if (Objects.nonNull(workspace)){
            return workspaceMapper.toDTO(workspace);
        }
        else throw new GenericNotFoundException("you can't enter this workspace",403);
    }

    @Override
    public List<WorkspaceResDTO> getAll(Long id) {
        List<Workspace> workspaceList = workspaceRepository.findAllBYID(getSessionId());
        List<WorkspaceResDTO> list = new ArrayList<>();
        for (Workspace workspace : workspaceList) {
            WorkspaceResDTO workspaceResDTO = workspaceMapper.toDTO(workspace);
            list.add(workspaceResDTO);
        }
        return list;
    }
    public List<WorkspaceResForBoardMember> getByBoardMemberId(){
        List<WorkspaceResForBoardMember> memberList = new ArrayList<>();
        List<Workspace> workspaceList = workspaceRepository.getAllForBoardUser(getSessionId());
        workspaceList.forEach(workspace -> {
            for (Board board : boardRepository.getByBoardUserId(workspace.getId(), getSessionId())) {
                BoardResDTO boardResDTO = boardMapper.toDTOFrom(board);
                boardResDTO.setColumnList(board.getColumns().stream().map(AbsDomain::getId).toList());
                boardResDTO.setMemberList(board.getUser().stream().map(AuthUser::getId).toList());
                memberList.add(WorkspaceResForBoardMember.builder()
                        .workspaceName(workspace.getName())
                        .boardResDTO(boardResDTO)
                        .build());
            }
        });
        return memberList;
    }
    @Override
    public void softDelete(Long id) {
        if (!workspaceRepository.softDeleted(id,getSessionId())){
            throw  new GenericNotFoundException("User can't authorize",403);
        }
    }
    @Override
    public void hardDelete(Long id) {
      if (!workspaceRepository.hardDeleted(id, getSessionId())){
          throw  new GenericNotFoundException("User can't authorize",403);
      }
    }
    public void addUser(AddMemberDTO addMemberDTO) {
        List<AuthUser> users = new ArrayList<>();
        Workspace found = workspaceRepository.findById(addMemberDTO.getSpaceId()).orElseThrow(() -> new GenericNotFoundException("Not found", 404));
        if (!found.getCreatedBy().equals(getSessionId())){
            throw new GenericNotFoundException("you aren't owner this workspace",403);
        }
        addMemberDTO.getUserList().forEach(id -> {
            AuthUser authUser = authUserRepository.findById(id).orElseThrow(() -> new GenericNotFoundException("Member not found", 404));
            users.add(authUser);
        } );
        users.addAll(found.getUser());
        found.setUser(users);
        workspaceRepository.save(found);
    }
}




