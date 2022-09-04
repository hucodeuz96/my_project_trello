package uz.hu.my_project_trello.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import uz.hu.my_project_trello.domains.project.Board;
import uz.hu.my_project_trello.dtos.project.board.BoardCreateDTO;
import uz.hu.my_project_trello.dtos.project.board.BoardResDTO;
import uz.hu.my_project_trello.dtos.project.board.BoardUpdateDTO;



/**
 * @author "Husniddin Ulachov"
 * @created 7:34 PM on 8/28/2022
 * @project my_project_trello
 */
@Mapper(componentModel = "spring")
public interface BoardMapper {
    Board fromDTO(BoardCreateDTO boardCreateDTO);

    BoardResDTO toDTOFrom(Board board);
    Board fromUDTO(BoardUpdateDTO boardUpdateDTO, @MappingTarget Board board);

}
