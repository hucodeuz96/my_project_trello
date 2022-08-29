package uz.hu.my_project_trello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.hu.my_project_trello.domains.auth.AuthUser;
import uz.hu.my_project_trello.response.ApiResponse;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public ApiResponse<List<AuthUser>> getAll() {
        return new ApiResponse<List<AuthUser>>(List.of(AuthUser.builder()
                .username("John")
                .password("123")
                .lastLoginTime(LocalDateTime.now())
                .email("john.lgd65@gmail.com")
                .build()), 2);
    }
}
