package uz.hu.my_project_trello.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import uz.hu.my_project_trello.domains.project.Board;
import uz.hu.my_project_trello.dtos.project.BoardCreateDTO;
import uz.hu.my_project_trello.dtos.project.BoardResDTO;
import uz.hu.my_project_trello.dtos.project.BoardUpdateDTO;

import java.lang.annotation.Target;

/**
 * @author "Husniddin Ulachov"
 * @created 7:34 PM on 8/28/2022
 * @project my_project_trello
 */
@Mapper(componentModel = "spring")
public interface BoardMapper {
    Board fromDTO(BoardCreateDTO boardCreateDTO);
    BoardResDTO toDTO (Board board);
    Board fromUDTO(BoardUpdateDTO boardUpdateDTO, @MappingTarget Board board);

    BoardResDTO twoResDTO(BoardResDTO  boardRes,@MappingTarget BoardResDTO boardResDTO);
}
