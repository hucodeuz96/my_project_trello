
package uz.hu.my_project_trello.controller.project;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.hu.my_project_trello.dtos.project.comment.CommentCreateDTO;
import uz.hu.my_project_trello.dtos.project.comment.CommentUpdateDTO;
import uz.hu.my_project_trello.services.project.CommentService;

/**
 * @author "Husniddin Ulachov"
 * @created 12:11 PM on 8/26/2022
 * @project my_project_trello
 */
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CommentCreateDTO commentCreateDTO){
        return ResponseEntity.ok().body(commentService.generate(commentCreateDTO));
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> edit( @RequestBody CommentUpdateDTO commentUpdateDTO){
        return ResponseEntity.ok().body(commentService.edit(commentUpdateDTO));
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<?> getAll(@PathVariable Long id ){
        return ResponseEntity.ok().body(commentService.getAll(id));
    }


    @DeleteMapping("/soft/{id}")
    public ResponseEntity<Boolean> softDeleted( Long id){
        commentService.softDelete(id);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Boolean> hardDeleted(Long id){
        commentService.hardDelete(id);
        return ResponseEntity.ok(true);
    }



}
