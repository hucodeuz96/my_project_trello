package uz.hu.my_project_trello.dtos.project.columin;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author "Husniddin Ulachov"
 * @created 12:20 PM on 8/26/2022
 * @project my_project_trello
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ColuminResDTO {
    private String name;
    private List<Long> cardIds;
    private Long boardId;
    private LocalDateTime createdAt ;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
    private boolean isDeleted;
    private Long orderNumber;
}
