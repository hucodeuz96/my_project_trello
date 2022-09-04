package uz.hu.my_project_trello.dtos.project.commit;

import lombok.*;
import uz.hu.my_project_trello.domains.project.Commit;

import java.time.LocalDateTime;
import java.util.HashMap;

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
public class CommitResDTO {
    private String text;
    private Long userId;
    private Long cardId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
    private boolean isDeleted;
}
