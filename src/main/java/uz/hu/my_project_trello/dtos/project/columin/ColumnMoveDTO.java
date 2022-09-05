package uz.hu.my_project_trello.dtos.project.columin;

import lombok.*;

/**
 * @author "Husniddin Ulachov"
 * @created 6:56 AM on 9/5/2022
 * @project my_project_trello
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ColumnMoveDTO {
   private Long fromColumnId;
   private Long toColumnId;
   private Long boardId;
}
