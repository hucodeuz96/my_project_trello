package uz.hu.my_project_trello.dtos.project.workspace;

import lombok.*;

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
public class WorkspaceCreateDTO {

    private String name;
    private String workspaceType;
    private String description;
    private String visibility;
    private HashSet<Long> userList;
}
