package uz.hu.my_project_trello.mappers;

import org.mapstruct.Mapper;
import uz.hu.my_project_trello.domains.auth.AuthUser;
import uz.hu.my_project_trello.dtos.UserRegisterDTO;
import uz.hu.my_project_trello.dtos.auth.AuthUserDTO;



@Mapper(componentModel = "spring")
public interface AuthUserMapper {
    AuthUser fromRegisterDTO(UserRegisterDTO dto);

    AuthUserDTO toDTO(AuthUser domain);
}
