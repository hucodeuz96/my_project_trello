package uz.hu.my_project_trello.dtos.project.card;

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
public class CardResDTO {
    private String name;
    private String description;
    private Long columinId;
    private LocalDateTime createdAt ;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
    private boolean isDeleted;
}
