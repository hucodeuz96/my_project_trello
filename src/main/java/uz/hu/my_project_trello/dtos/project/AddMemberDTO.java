package uz.hu.my_project_trello.dtos.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author "Husniddin Ulachov"
 * @created 7:22 AM on 8/27/2022
 * @project my_project_trello
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMemberDTO {
    private Long spaceId;
    private List<Long> userList;
}
