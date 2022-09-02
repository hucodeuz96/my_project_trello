package uz.hu.my_project_trello.mappers;

import org.mapstruct.Mapper;
import uz.hu.my_project_trello.domains.project.Columin;
import uz.hu.my_project_trello.dtos.project.ColuminResDTO;

/**
 * @author "Husniddin Ulachov"
 * @created 10:47 PM on 8/30/2022
 * @project my_project_trello
 */
@Mapper(componentModel = "spring")
public interface ColuminMapper {
    ColuminResDTO from(Columin columin);
}
