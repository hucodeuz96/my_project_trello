package uz.hu.my_project_trello.domains.project;

import lombok.*;
import uz.hu.my_project_trello.domains.auth.AuthUser;

import javax.persistence.*;

/**
 * @author "Husniddin Ulachov"
 * @created 4:57 PM on 8/24/2022
 * @project my_project_trello
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment extends AbsDomain {

    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private AuthUser authUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private Card card;

}
