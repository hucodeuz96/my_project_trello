package uz.hu.my_project_trello.dtos.project.workspace;

import lombok.*;

/**
 * @author "Husniddin Ulachov"
 * @created 6:34 PM on 8/26/2022
 * @project my_project_trello
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class WorkspaceUpdateDTO {
    private Long id;
    private String name;
    private String workspaceType;
    private String description;
    private String visibility;
}
