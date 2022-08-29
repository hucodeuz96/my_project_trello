package uz.hu.my_project_trello.services.auth;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uz.hu.my_project_trello.domains.auth.AuthRole;
import uz.hu.my_project_trello.dtos.auth.AuthRoleCreateDTO;
import uz.hu.my_project_trello.dtos.auth.AuthRoleDTO;
import uz.hu.my_project_trello.exceptions.GenericNotFoundException;
import uz.hu.my_project_trello.mappers.AuthRoleMapper;
import uz.hu.my_project_trello.repository.AuthRoleRepository;

import java.util.List;
import java.util.function.Supplier;



@Service
@RequiredArgsConstructor
public class AuthRoleService {

    private final AuthRoleRepository authRoleRepository;
    private final AuthRoleMapper authRoleMapper;


    @PreAuthorize("hasAuthority(T(uz.jl.springbootfeatures.enums.Permissions).ROLE_READ)")
    public List<AuthRoleDTO> getAll() {
        List<AuthRole> authRoles = authRoleRepository.findAll();
        return authRoleMapper.toDTO(authRoles);
    }

    @PreAuthorize("hasAuthority(T(uz.hu.my_project_trello.enums.Permissions).ROLE_READ)")
    public AuthRoleDTO get(@NonNull Long id) {
        Supplier<GenericNotFoundException> notFoundException = () -> new GenericNotFoundException("Role not found", 404);
        AuthRole authRole = authRoleRepository.findById(id).orElseThrow(notFoundException);
        return authRoleMapper.toDTO(authRole);
    }

    @PreAuthorize("hasAuthority(T(uz.hu.my_project_trello.enums.Permissions).ROLE_CREATE)")
    public void create(AuthRoleCreateDTO dto) {
        AuthRole authRole = authRoleMapper.fromCreateDTO(dto);
        authRoleRepository.save(authRole);
    }

    @PreAuthorize("hasAuthority(T(uz.hu.my_project_trello.enums.Permissions).ROLE_DELETE)")
    public void delete(@NonNull Long id) {
        authRoleRepository.deleteById(id);
    }
}
