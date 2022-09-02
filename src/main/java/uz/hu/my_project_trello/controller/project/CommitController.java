
package uz.hu.my_project_trello.controller.project;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.hu.my_project_trello.dtos.project.AddMemberDTO;
import uz.hu.my_project_trello.dtos.project.CardCreateDTO;
import uz.hu.my_project_trello.dtos.project.CardResDTO;
import uz.hu.my_project_trello.dtos.project.CardUpdateDTO;
import uz.hu.my_project_trello.response.ApiResponse;
import uz.hu.my_project_trello.services.project.CardService;
import uz.hu.my_project_trello.services.project.CommitService;

/**
 * @author "Husniddin Ulachov"
 * @created 12:11 PM on 8/26/2022
 * @project my_project_trello
 */
@RestController
@RequestMapping("/commit")
@RequiredArgsConstructor
public class CommitController {
    private final CommitService commitService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CardCreateDTO cardCreateDTO){
        return ResponseEntity.ok().body(cardService.generate(cardCreateDTO));
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> edit( @RequestBody CardUpdateDTO cardUpdateDTO){
        return ResponseEntity.ok().body(cardService.edit(cardUpdateDTO));
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<?> getAll(@PathVariable Long id ){
        return ResponseEntity.ok().body(cardService.getAll(id));
    }

    @GetMapping("/{id}")
    public ApiResponse<CardResDTO> getOne(@PathVariable("id") Long id){
        return new ApiResponse<>(cardService.getOne(id));
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<Boolean> softDeleted( Long id){
        cardService.softDelete(id);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Boolean> hardDeleted(Long id){
        cardService.hardDelete(id);
        return ResponseEntity.ok(true);
    }
    @PostMapping("/addMember")
    public  ResponseEntity<Boolean> addUser(@RequestBody AddMemberDTO addMemberDTO){
        cardService.addUser(addMemberDTO);
        return ResponseEntity.ok(true);
    }



}
