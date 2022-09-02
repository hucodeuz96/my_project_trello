package uz.hu.my_project_trello.mappers;

import org.mapstruct.Mapper;
import uz.hu.my_project_trello.domains.project.Card;
import uz.hu.my_project_trello.dtos.project.CardResDTO;

/**
 * @author "Husniddin Ulachov"
 * @created 6:49 PM on 9/1/2022
 * @project my_project_trello
 */
@Mapper(componentModel = "spring")
public interface CardMapper {
    CardResDTO fromCard(Card card);
}
