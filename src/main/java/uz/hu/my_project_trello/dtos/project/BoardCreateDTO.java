package uz.hu.my_project_trello.dtos.project;

import lombok.*;
import uz.hu.my_project_trello.domains.project.Workspace;

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
public class BoardCreateDTO {
    private String name;
    private Long workscapeId;
    private String visibility;
}
