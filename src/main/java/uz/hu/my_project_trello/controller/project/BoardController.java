
package uz.hu.my_project_trello.controller.project;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.hu.my_project_trello.dtos.project.AddMemberDTO;
import uz.hu.my_project_trello.dtos.project.board.BoardCreateDTO;
import uz.hu.my_project_trello.dtos.project.board.BoardResDTO;
import uz.hu.my_project_trello.dtos.project.board.BoardUpdateDTO;
import uz.hu.my_project_trello.response.ApiResponse;
import uz.hu.my_project_trello.services.project.BoardService;

import java.util.Objects;

/**
 * @author "Husniddin Ulachov"
 * @created 12:11 PM on 8/26/2022
 * @project my_project_trello
 */
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody BoardCreateDTO boardCreateDTO){
        return ResponseEntity.ok().body(boardService.generate(boardCreateDTO));
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> edit( @RequestBody BoardUpdateDTO  boardUpdateDTO){
        return ResponseEntity.ok().body(boardService.edit(boardUpdateDTO));
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<?> getAll(@PathVariable Long id ){
        return ResponseEntity.ok().body(boardService.getAll(id));
    }

    @GetMapping("/{id}")
    public ApiResponse<BoardResDTO> getOne(@PathVariable("id") Long id){
        return new ApiResponse<>(boardService.getOne(id));
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<Boolean> softDeleted( Long id){
        boardService.softDelete(id);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Boolean> hardDeleted(Long id){
        boardService.hardDelete(id);
        return ResponseEntity.ok(true);
    }
    @PostMapping("/addMember")
    public  ResponseEntity<Boolean> addUser(@RequestBody AddMemberDTO addMemberDTO){
        boardService.addUser(addMemberDTO);
      return ResponseEntity.ok(true);
    }
    @GetMapping("/getMember")
    public ResponseEntity<?> getCardMember(@RequestParam Long boardId){
        return ResponseEntity.status(Objects.nonNull(boardService.getUserBYBoardId(boardId))?200:409).body(boardService.getUserBYBoardId(boardId));
    }




}
