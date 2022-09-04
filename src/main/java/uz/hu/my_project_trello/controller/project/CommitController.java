
package uz.hu.my_project_trello.controller.project;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.hu.my_project_trello.dtos.project.commit.CommitCreateDTO;
import uz.hu.my_project_trello.dtos.project.commit.CommitUpdateDTO;
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
    public ResponseEntity<?> create(@RequestBody CommitCreateDTO commitCreateDTO){
        return ResponseEntity.ok().body(commitService.generate(commitCreateDTO));
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> edit( @RequestBody CommitUpdateDTO commitUpdateDTO){
        return ResponseEntity.ok().body(commitService.edit(commitUpdateDTO));
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<?> getAll(@PathVariable Long id ){
        return ResponseEntity.ok().body(commitService.getAll(id));
    }


    @DeleteMapping("/soft/{id}")
    public ResponseEntity<Boolean> softDeleted( Long id){
        commitService.softDelete(id);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Boolean> hardDeleted(Long id){
        commitService.hardDelete(id);
        return ResponseEntity.ok(true);
    }



}
