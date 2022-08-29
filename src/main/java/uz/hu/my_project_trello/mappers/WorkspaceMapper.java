package uz.hu.my_project_trello.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import uz.hu.my_project_trello.domains.project.Workspace;
import uz.hu.my_project_trello.dtos.project.WorkspaceCreateDTO;
import uz.hu.my_project_trello.dtos.project.WorkspaceResDTO;
import uz.hu.my_project_trello.dtos.project.WorkspaceUpdateDTO;

/**
 * @author "Husniddin Ulachov"
 * @created 2:37 PM on 8/26/2022
 * @project my_project_trello
 */
@Mapper(componentModel = "spring")
public interface WorkspaceMapper {
    Workspace fromDTO(WorkspaceCreateDTO workspace);
    WorkspaceResDTO toDTO(Workspace workspace);
    Workspace fromUDTO(WorkspaceUpdateDTO udto, @MappingTarget Workspace workspace);

}
