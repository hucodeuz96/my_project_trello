package uz.hu.my_project_trello.services.project;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.hu.my_project_trello.domains.auth.AuthUser;
import uz.hu.my_project_trello.domains.project.Board;
import uz.hu.my_project_trello.domains.project.Workspace;
import uz.hu.my_project_trello.dtos.project.AddMemberDTO;
import uz.hu.my_project_trello.dtos.project.board.BoardCreateDTO;
import uz.hu.my_project_trello.dtos.project.board.BoardResDTO;
import uz.hu.my_project_trello.dtos.project.board.BoardUpdateDTO;
import uz.hu.my_project_trello.exceptions.GenericNotFoundException;
import uz.hu.my_project_trello.mappers.BoardMapper;
import uz.hu.my_project_trello.repository.AuthUserRepository;
import uz.hu.my_project_trello.repository.BoardRepository;
import uz.hu.my_project_trello.repository.WorkspaceRepository;
import uz.hu.my_project_trello.services.auth.GetSessionUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private final GetSessionUser session;
    @Override
    public BoardResDTO generate(BoardCreateDTO boardCreateDTO) {

        Workspace workspace = workspaceRepository.findById(boardCreateDTO.getWorkscapeId()).orElseThrow(() -> new GenericNotFoundException("Not found Workspace", 404));
        if (Objects.equals(workspace.getCreatedBy(), session.getUser().getId())){
            Board board = boardMapper.fromDTO(boardCreateDTO);
            board.setWorkspace(workspace);
            board.setCreatedBy(session.getUser().getId());
            AuthUser authUser = authUserRepository.findById(session.getUser().getId()).orElseThrow(() -> new GenericNotFoundException("Not Found", 404));
            board.setUser(List.of(authUser));
            Board save = boardRepository.save(board);
            BoardResDTO boardResDTO = boardMapper.toDTOFrom(save);
            boardResDTO.setWorkspaceId(workspace.getId());
            return boardResDTO;
        }
        else throw new GenericNotFoundException("Server refuses you to authorize",403);

    }
    @Override
    public BoardResDTO edit(BoardUpdateDTO boardUpdateDTO) {
        Board board = boardRepository.findById(boardUpdateDTO.getId()).orElseThrow(() -> new GenericNotFoundException("Not found Board", 404));
        if (board.getCreatedBy().equals(session.getUser().getId())) {
            Board udto = boardMapper.fromUDTO(boardUpdateDTO, board);
            udto.setUpdatedAt(LocalDateTime.now());
            udto.setUpdatedBy(session.getUser().getId());
            Board save = boardRepository.save(udto);
            BoardResDTO boardResDTO = boardMapper.toDTOFrom(save);
            BoardResDTO boardResDTO1 = setUserAndColumn(board);
            boardResDTO.setColumnList(boardResDTO1.getColumnList());
            boardResDTO.setMemberList(boardResDTO1.getMemberList());
            boardResDTO.setWorkspaceId(save.getWorkspace().getId());
            return boardResDTO;
        }
        else throw new GenericNotFoundException("Not found Board", 404);
    }

    @Override
    public BoardResDTO getOne(Long id) {
        Board board = boardRepository.getOneBYId(session.getUser().getId(), id).orElseThrow(() -> new GenericNotFoundException("Server refused to visit board", 403));
        BoardResDTO boardResDTO = boardMapper.toDTOFrom(board);
        BoardResDTO boardResDTO1 = setUserAndColumn(board);
        boardResDTO.setColumnList(boardResDTO1.getColumnList());
        boardResDTO.setMemberList(boardResDTO1.getMemberList());
        boardResDTO.setWorkspaceId(board.getWorkspace().getId());
        return boardResDTO;
    }
    @Override
    public List<BoardResDTO> getAll(Long id) {
        List<Board> all = boardRepository.getAllByWorkspaceId(session.getUser().getId(), id);
        if (all.size()!=0){
            List<BoardResDTO> boardResDTOS = new ArrayList<>();
            for (Board board : all) {
                BoardResDTO boardResDTO = boardMapper.toDTOFrom(board);
                BoardResDTO boardResDTO1 = setUserAndColumn(board);
                boardResDTO.setWorkspaceId(board.getWorkspace().getId());
                boardResDTO.setMemberList(boardResDTO1.getMemberList());
                boardResDTO.setColumnList(boardResDTO1.getColumnList());
                boardResDTOS.add(boardResDTO);
            }
            return boardResDTOS;
        }
        else throw new GenericNotFoundException("You aren't member or owner", 403);
    }

    @Override
    public void softDelete(Long id) {
        boardRepository.softDeleteBoardId(session.getUser().getId(),id);
    }

    @Override
    public void hardDelete(Long id) {
        boardRepository.hardDeleteBoardId(session.getUser().getId(), id);
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

    private  BoardResDTO setUserAndColumn(Board board) {
        BoardResDTO boardResDTO = new BoardResDTO();
        if (board.getUser()!=null) {
            boardResDTO.setMemberList(board.getUser().stream().map(AuthUser::getId).collect(Collectors.toList()));
        }
        List<Long> columunsId = new ArrayList<>();
        if (!Objects.isNull(board.getColumns())) {
            board.getColumns().forEach(columin -> {
                columunsId.add(columin.getId());
            });
            boardResDTO.setColumnList(columunsId);
        }
        return boardResDTO;
    }
    public   List<AuthUser> getUserBYBoardId(Long id){
        Board board = boardRepository.getOneBYId(session.getUser().getId(), id).orElseThrow(() -> new GenericNotFoundException("You aren't board member", 403));
        return board.getUser();
    }
}

