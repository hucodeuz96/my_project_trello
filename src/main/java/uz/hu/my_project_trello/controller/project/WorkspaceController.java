package uz.hu.my_project_trello.controller.project;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.hu.my_project_trello.config.security.UserDetails;
import uz.hu.my_project_trello.dtos.project.AddMemberDTO;
import uz.hu.my_project_trello.dtos.project.workspace.WorkspaceResDTO;
import uz.hu.my_project_trello.dtos.project.workspace.WorkspaceUpdateDTO;
import uz.hu.my_project_trello.dtos.project.workspace.WorkspaceCreateDTO;
import uz.hu.my_project_trello.response.ApiResponse;
import uz.hu.my_project_trello.services.project.WorkspaceService;

/**
 * @author "Husniddin Ulachov"
 * @created 12:11 PM on 8/26/2022
 * @project my_project_trello
 */
@RestController
@RequestMapping("/workspace")
@RequiredArgsConstructor
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody WorkspaceCreateDTO workspaceCreateDTO){
        return ResponseEntity.ok().body(workspaceService.generate(workspaceCreateDTO));
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> edit( @RequestBody WorkspaceUpdateDTO workspaceDTO){
        return ResponseEntity.ok().body(workspaceService.edit(workspaceDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){

        return ResponseEntity.ok().body(workspaceService.getAll(null));
    }

    @GetMapping("/{id}")
    public ApiResponse<WorkspaceResDTO> getOne(@PathVariable("id") Long id){
        return new ApiResponse<>(workspaceService.getOne(id));
    }
    @GetMapping("/getBoardMember")
    public ResponseEntity<?> getBoardMember(){
        return ResponseEntity.ok(workspaceService.getByBoardMemberId());
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<Boolean> softDeleted( Long id){
        workspaceService.softDelete(id);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Boolean> hardDeleled(Long id){
        workspaceService.hardDelete(id);
        return ResponseEntity.ok(true);
    }
    @PostMapping("/addMember")
    public  ResponseEntity<Boolean> addUser(@RequestBody AddMemberDTO addMemberDTO){
        workspaceService.addUser(addMemberDTO);
      return ResponseEntity.ok(true);
    }



}
