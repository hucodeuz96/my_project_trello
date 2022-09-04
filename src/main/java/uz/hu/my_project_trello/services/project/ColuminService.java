package uz.hu.my_project_trello.services.project;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.hu.my_project_trello.domains.auth.AuthUser;
import uz.hu.my_project_trello.domains.project.Board;
import uz.hu.my_project_trello.domains.project.Card;
import uz.hu.my_project_trello.domains.project.Columin;
import uz.hu.my_project_trello.dtos.project.columin.ColuminCreateDTO;
import uz.hu.my_project_trello.dtos.project.columin.ColuminResDTO;
import uz.hu.my_project_trello.dtos.project.columin.ColuminUpdateDTO;
import uz.hu.my_project_trello.exceptions.GenericNotFoundException;
import uz.hu.my_project_trello.mappers.ColuminMapper;
import uz.hu.my_project_trello.repository.AuthUserRepository;
import uz.hu.my_project_trello.repository.BoardRepository;
import uz.hu.my_project_trello.repository.ColuminRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author "Husniddin Ulachov"
 * @created 6:45 PM on 8/30/2022
 * @project my_project_trello
 */
@Service
@RequiredArgsConstructor
public class ColuminService extends AbsProjectService<ColuminResDTO, ColuminUpdateDTO, ColuminCreateDTO,Long> {
    private final ColuminRepository columinRepository;
    private final BoardRepository boardRepository;
    private final ColuminMapper columinMapper;
    private final AuthUserRepository authUserRepository;

    public AuthUser getSessionUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
       return authUserRepository.findByUsername(name).orElseThrow(() -> new GenericNotFoundException("not Authorization", 403));
    }
    @Override
    public ColuminResDTO generate(ColuminCreateDTO columinCreateDTO) {
        Board board = boardRepository.getOneBYId(getSessionUser().getId(), columinCreateDTO.getBoard()).orElseThrow(() -> new GenericNotFoundException("Server refused you to visit board", 403));
        Columin columin = new Columin();
        columin.setBoard(board);
        columin.setName(columinCreateDTO.getName());
        columin.setCreatedBy(getSessionUser().getId());
        Columin save = columinRepository.save(columin);
        ColuminResDTO columinResDTO = columinMapper.fromColumin(save);
        columinResDTO.setBoardId(board.getId());
        return columinResDTO;
    }

    @Override
    public ColuminResDTO edit(ColuminUpdateDTO columinUpdateDTO) {
        boardRepository.getOneBYId(getSessionUser().getId(), columinUpdateDTO.getBoardId()).orElseThrow(() -> new GenericNotFoundException("Server refused you to visit board", 403));
        Columin columin = columinRepository.findById(columinUpdateDTO.getId()).orElseThrow(() -> new GenericNotFoundException("Not found columin", 404));
        columin.setName(columinUpdateDTO.getName());
        columin.setUpdatedAt(LocalDateTime.now());
        columin.setUpdatedBy(getSessionUser().getId());
        ColuminResDTO columinResDTO = columinMapper.fromColumin(columinRepository.save(columin));
        columinResDTO.setBoardId(columinUpdateDTO.getBoardId());
        return columinResDTO;
    }

    @Override
    public ColuminResDTO getOne(Long id) {
        Columin columin = columinRepository.getOneByColuminID(id).orElseThrow(() -> new GenericNotFoundException("Not found Columin",404));
        boardRepository.getOneBYId(getSessionUser().getId(), columin.getBoard().getId()).orElseThrow(() -> new GenericNotFoundException("Server refused you to visit board", 403));
        ColuminResDTO columinResDTO = columinMapper.fromColumin(columin);
        columinResDTO.setBoardId(columin.getBoard().getId());
        columinResDTO.setCardIds(columin.getCard().stream().map(Card::getId).collect(Collectors.toList()));
        return columinResDTO;
    }

    @Override
    public List<ColuminResDTO> getAll(Long id) {
        boardRepository.getOneBYId(getSessionUser().getId(),id).orElseThrow(() -> new GenericNotFoundException("Server refused you to visit board", 403));
        List<ColuminResDTO> columinResDTOS = new ArrayList<>();
        for (Columin columin : columinRepository.getAllByBoardID(id)) {
            ColuminResDTO from = columinMapper.fromColumin(columin);
            from.setBoardId(columin.getBoard().getId());
            from.setCardIds(columin.getCard().stream().map(Card::getId).collect(Collectors.toList()));
            columinResDTOS.add(from);
        }
        return columinResDTOS;
    }

    @Override
    public void softDelete(Long id) {
        columinRepository.softDeleteByColumnID(id);
    }

    @Override
    public void hardDelete(Long id) {
        columinRepository.deleteById(id);
    }
}
