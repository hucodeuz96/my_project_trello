
package uz.hu.my_project_trello.controller.project;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.hu.my_project_trello.dtos.project.columin.ColuminCreateDTO;
import uz.hu.my_project_trello.dtos.project.columin.ColuminResDTO;
import uz.hu.my_project_trello.dtos.project.columin.ColuminUpdateDTO;
import uz.hu.my_project_trello.dtos.project.columin.ColumnMoveDTO;
import uz.hu.my_project_trello.response.ApiResponse;
import uz.hu.my_project_trello.services.project.ColuminService;

/**
 * @author "Husniddin Ulachov"
 * @created 12:11 PM on 8/26/2022
 * @project my_project_trello
 */
@RestController
@RequestMapping("/columin")
@RequiredArgsConstructor
public class ColuminController {
    private final ColuminService columinService;
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ColuminCreateDTO columinCreateDTO){
        return ResponseEntity.ok().body(columinService.generate(columinCreateDTO));
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> edit( @RequestBody ColuminUpdateDTO columinUpdateDTO){
        return ResponseEntity.ok().body(columinService.edit(columinUpdateDTO));
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<?> getAll(@PathVariable Long id ){
        return ResponseEntity.ok().body(columinService.getAll(id));
    }

    @GetMapping("/{id}")
    public ApiResponse<ColuminResDTO> getOne(@PathVariable("id") Long id){
        return new ApiResponse<>(columinService.getOne(id));
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<Boolean> softDeleted( Long id){
        columinService.softDelete(id);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Boolean> hardDeleted(Long id){
        columinService.hardDelete(id);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/moveColumn")
    public ResponseEntity<?> moveColumn(@RequestBody ColumnMoveDTO columnMoveDTO){
        columinService.moveColumnById(columnMoveDTO);
        return ResponseEntity.ok().body(true);
    }




}
