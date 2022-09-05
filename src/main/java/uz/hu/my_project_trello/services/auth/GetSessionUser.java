package uz.hu.my_project_trello.services.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.hu.my_project_trello.domains.auth.AuthUser;
import uz.hu.my_project_trello.exceptions.GenericNotFoundException;
import uz.hu.my_project_trello.repository.AuthUserRepository;

/**
 * @author "Husniddin Ulachov"
 * @created 5:46 AM on 9/5/2022
 * @project my_project_trello
 */
@Component
@RequiredArgsConstructor
public class GetSessionUser {
    private final AuthUserRepository authUserRepository;
    public AuthUser getUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return authUserRepository.findByUsername(name).orElseThrow(() -> new GenericNotFoundException("not Authorization", 403));
    }
}
