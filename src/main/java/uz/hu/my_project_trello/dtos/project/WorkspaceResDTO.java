package uz.hu.my_project_trello.dtos.project;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;

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
public class WorkspaceResDTO {
    private String name;
    private String workspaceType;
    private String description;
    private String visibility;
    private LocalDateTime createdAt;
    private Long createdBy;
}
