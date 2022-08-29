package uz.hu.my_project_trello.services.project;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.hu.my_project_trello.domains.auth.AuthUser;
import uz.hu.my_project_trello.domains.project.Board;
import uz.hu.my_project_trello.domains.project.Workspace;
import uz.hu.my_project_trello.dtos.project.AddMemberDTO;
import uz.hu.my_project_trello.dtos.project.BoardCreateDTO;
import uz.hu.my_project_trello.dtos.project.BoardResDTO;
import uz.hu.my_project_trello.dtos.project.BoardUpdateDTO;
import uz.hu.my_project_trello.exceptions.GenericNotFoundException;
import uz.hu.my_project_trello.mappers.BoardMapper;
import uz.hu.my_project_trello.repository.AuthUserRepository;
import uz.hu.my_project_trello.repository.BoardRepository;
import uz.hu.my_project_trello.repository.WorkspaceRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author "Husniddin Ulachov"
 * @created 6:15 PM on 8/26/2022
 * @project my_project_trello
 */
@RequiredArgsConstructor
@Service
public class BoardService extends AbsProjectService<BoardResDTO, BoardUpdateDTO, BoardCreateDTO,Long> {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final BoardMapper boardMapper;
    private final AuthUserRepository authUserRepository;
    private final  Cloneable cloneable;

    public Long getSessionId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        AuthUser authUser = authUserRepository.findByUsername(name).orElseThrow(() -> new GenericNotFoundException("not Authorization", 403));
        return authUser.getId();
    }

    @Override
    public BoardResDTO generate(BoardCreateDTO boardCreateDTO) {
        workspaceRepository.findById(boardCreateDTO.getWorkscapeId()).orElseThrow(() -> new GenericNotFoundException("Not found Workspace", 404));
        Board board = boardMapper.fromDTO(boardCreateDTO);
        Workspace workspace = workspaceRepository.findById(boardCreateDTO.getWorkscapeId()).orElseThrow(() -> new GenericNotFoundException("Not found Workspace", 404));
        board.setWorkspace(workspace);
        board.setCreatedBy(getSessionId());
        Board save = boardRepository.save(board);
        BoardResDTO boardResDTO = boardMapper.toDTO(save);
        System.out.println(boardResDTO);
        boardResDTO.setWorkscape(workspace.getId());
        return boardResDTO;
    }

    @Override
    public BoardResDTO edit(BoardUpdateDTO boardUpdateDTO) {
        Board board = boardRepository.findById(boardUpdateDTO.getId()).orElseThrow(() -> new GenericNotFoundException("Not found Board", 404));
        if (board.getCreatedBy().equals(getSessionId())) {
            Board udto = boardMapper.fromUDTO(boardUpdateDTO, board);
            udto.setUpdatedAt(LocalDateTime.now());
            udto.setUpdatedBy(getSessionId());
            Board save = boardRepository.save(udto);
            BoardResDTO boardResDTO = boardMapper.toDTO(save);

            return boardResDTO;
        } else throw new GenericNotFoundException("Not found Board", 404);
    }

    @Override
    public BoardResDTO getOne(Long id) {
        Board board = boardRepository.getOne(getSessionId(), id);
        BoardResDTO boardResDTO = boardMapper.toDTO(board);
        BoardResDTO boardResDTO1 = setUserAndColumn(board);
        return boardMapper.twoResDTO(boardResDTO,boardResDTO1);
    }


    @Override
    public List<BoardResDTO> getAll(Long id) {
        boardRepository.getAll(getSessionId(), id);
        return null;
    }

    @Override
    public void softDelete(Long id) {
        if (!boardRepository.softDelete(getSessionId(), id)) {
            throw new GenericNotFoundException("User can't authorize", 403);
        }
    }

    @Override
    public void hardDelete(Long id) {
        if (!boardRepository.hardDelete(getSessionId(), id)) {
            throw new GenericNotFoundException("User can't authorize", 403);
        }
    }

    public void addUser(AddMemberDTO addMemberDTO) {
        List<AuthUser> authUsers = new ArrayList<>();
        Board board = boardRepository.findById(addMemberDTO.getSpaceId()).orElseThrow(() -> new GenericNotFoundException("Not found Board", 404));
        authUsers.addAll(board.getUser());
        addMemberDTO.getUserList().forEach(id -> {
            AuthUser authUser = authUserRepository.findById(id).orElseThrow(() -> new GenericNotFoundException("Member not found", 404));
            authUsers.add(authUser);
        });
        board.setUser(authUsers);
        boardRepository.save(board);
    }

    private static BoardResDTO setUserAndColumn(Board board) {
        List<String> userList = new ArrayList<>();
        BoardResDTO boardResDTO = new BoardResDTO();
        if (Objects.nonNull(board.getUser())) {
            board.getUser().forEach(authUser -> {
                userList.add(authUser.getUsername());
            });
            boardResDTO.setUser(userList);
        }
        List<Long> columunsId = new ArrayList<>();
        if (Objects.nonNull(board.getColumns())) {
            board.getColumns().forEach(columin -> {
                columunsId.add(columin.getId());
            });
            boardResDTO.setColumns(columunsId);
        }
        return boardResDTO;

    }
}
