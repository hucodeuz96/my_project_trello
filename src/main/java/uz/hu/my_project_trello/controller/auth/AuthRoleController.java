package uz.hu.my_project_trello.controller.auth;

import org.springframework.web.bind.annotation.*;
import uz.hu.my_project_trello.controller.ApiController;
import uz.hu.my_project_trello.dtos.auth.AuthRoleCreateDTO;
import uz.hu.my_project_trello.dtos.auth.AuthRoleDTO;
import uz.hu.my_project_trello.response.ApiResponse;
import uz.hu.my_project_trello.services.auth.AuthRoleService;

import javax.validation.Valid;
import java.util.List;


@RestController
public class AuthRoleController extends ApiController<AuthRoleService> {


    protected AuthRoleController(AuthRoleService service) {
        super(service);
    }

    @GetMapping(PATH + "/role")
    public ApiResponse<List<AuthRoleDTO>> getAll() {
        return new ApiResponse<>(service.getAll());
    }

    @GetMapping(PATH + "/role/{id}")
    public ApiResponse<AuthRoleDTO> get(@PathVariable Long id) {
        return new ApiResponse<>(service.get(id));
    }

    @PostMapping(PATH + "/role")
    public ApiResponse<Void> create(@Valid @RequestBody AuthRoleCreateDTO dto) {
        service.create(dto);
        return new ApiResponse<>(201);
    }

    @DeleteMapping(PATH + "/role/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ApiResponse<>(204);
    }


}
