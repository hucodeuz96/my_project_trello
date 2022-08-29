package uz.hu.my_project_trello.mappers;

import org.mapstruct.Mapper;
import uz.hu.my_project_trello.domains.auth.AuthRole;
import uz.hu.my_project_trello.dtos.auth.AuthRoleCreateDTO;
import uz.hu.my_project_trello.dtos.auth.AuthRoleDTO;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AuthRoleMapper {
    AuthRoleDTO toDTO(AuthRole entity);

    List<AuthRoleDTO> toDTO(List<AuthRole> entities);

    AuthRole fromCreateDTO(AuthRoleCreateDTO dto);
}
