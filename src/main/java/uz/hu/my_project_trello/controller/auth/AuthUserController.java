package uz.hu.my_project_trello.controller.auth;

import org.springframework.web.bind.annotation.*;
import uz.hu.my_project_trello.controller.ApiController;
import uz.hu.my_project_trello.domains.auth.AuthUser;
import uz.hu.my_project_trello.dtos.JwtResponse;
import uz.hu.my_project_trello.dtos.LoginRequest;
import uz.hu.my_project_trello.dtos.RefreshTokenRequest;
import uz.hu.my_project_trello.dtos.UserRegisterDTO;
import uz.hu.my_project_trello.response.ApiResponse;
import uz.hu.my_project_trello.services.AuthUserService;

import javax.validation.Valid;



@RestController
public class AuthUserController extends ApiController<AuthUserService> {
    protected AuthUserController(AuthUserService service) {
        super(service);
    }

    @PostMapping(value = PATH + "/auth/login", produces = "application/json")
    public ApiResponse<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ApiResponse<>(service.login(loginRequest));
    }

    @GetMapping(value = PATH + "/auth/refresh", produces = "application/json")
    public ApiResponse<JwtResponse> login(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return new ApiResponse<>(service.refreshToken(refreshTokenRequest));
    }

    @PostMapping(PATH + "/auth/register")
    public ApiResponse<AuthUser> register(@Valid @RequestBody UserRegisterDTO dto) {
        return new ApiResponse<>(service.register(dto));
    }

    @GetMapping(PATH + "/auth/activate")
    public ApiResponse<Boolean> register(@RequestParam(name = "activation_code") String activationCode) {
        return new ApiResponse<>(service.activateUser(activationCode));
    }

    @GetMapping(PATH + "/auth/me")
    public AuthUser me() {
        return null;
    }
}
