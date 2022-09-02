package uz.hu.my_project_trello.services.project;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.hu.my_project_trello.domains.auth.AuthUser;
import uz.hu.my_project_trello.domains.project.Board;
import uz.hu.my_project_trello.domains.project.Columin;
import uz.hu.my_project_trello.dtos.project.ColuminCreateDTO;
import uz.hu.my_project_trello.dtos.project.ColuminResDTO;
import uz.hu.my_project_trello.dtos.project.ColuminUpdateDTO;
import uz.hu.my_project_trello.exceptions.GenericNotFoundException;
import uz.hu.my_project_trello.mappers.ColuminMapper;
import uz.hu.my_project_trello.repository.AuthUserRepository;
import uz.hu.my_project_trello.repository.BoardRepository;
import uz.hu.my_project_trello.repository.ColuminRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public Long getSessionId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        AuthUser authUser = authUserRepository.findByUsername(name).orElseThrow(() -> new GenericNotFoundException("not Authorization", 403));
        return authUser.getId();
    }
    @Override
    public ColuminResDTO generate(ColuminCreateDTO columinCreateDTO) {
        Board board = boardRepository.findById(columinCreateDTO.getBoard()).orElseThrow(() -> new GenericNotFoundException("Not found board", 404));
        Columin columin = new Columin();
        columin.setBoard(board);
        columin.setName(columinCreateDTO.getName());
        columin.setCreatedBy(getSessionId());
        Columin save = columinRepository.save(columin);
        return columinMapper.from(save);
    }

    @Override
    public ColuminResDTO edit(ColuminUpdateDTO columinUpdateDTO) {
        Columin columin = columinRepository.findById(columinUpdateDTO.getId()).orElseThrow(() -> new GenericNotFoundException("Not found columin", 404));
        columin.setName(columinUpdateDTO.getName());
        columin.setUpdatedAt(LocalDateTime.now());
        columin.setUpdatedBy(getSessionId());
        return columinMapper.from(columinRepository.save(columin));
    }

    @Override
    public ColuminResDTO getOne(Long id) {
        Columin columin = columinRepository.getOne(id);
        return columinMapper.from(columin);
    }

    @Override
    public List<ColuminResDTO> getAll(Long id) {
        List<ColuminResDTO> columinResDTOS = new ArrayList<>();
        for (Columin columin : columinRepository.getAll(id)) {
            ColuminResDTO from = columinMapper.from(columin);
            columinResDTOS.add(from);
        }
        return columinResDTOS;
    }

    @Override
    public void softDelete(Long id) {
        columinRepository.softDelete(id);
    }

    @Override
    public void hardDelete(Long id) {
        columinRepository.deleteById(id);
    }
}
