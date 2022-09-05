package uz.hu.my_project_trello.dtos.project.card;

import lombok.*;

/**
 * @author "Husniddin Ulachov"
 * @created 5:58 PM on 9/4/2022
 * @project my_project_trello
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CardMoveDTO {
    private Long cardId;
    private Long columinId;
}
