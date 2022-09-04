package uz.hu.my_project_trello.dtos.project.workspace;

import lombok.*;
import uz.hu.my_project_trello.domains.project.Board;
import uz.hu.my_project_trello.dtos.project.board.BoardResDTO;

import java.time.LocalDateTime;

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
public class WorkspaceResForBoardMember {
    private String workspaceName;
    private BoardResDTO boardResDTO;
}
