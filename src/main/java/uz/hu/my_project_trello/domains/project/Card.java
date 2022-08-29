package uz.hu.my_project_trello.domains.project;

import lombok.*;
import uz.hu.my_project_trello.domains.auth.AuthUser;

import javax.persistence.*;
import java.util.List;

/**
 * @author "Husniddin Ulachov"
 * @created 5:37 PM on 8/24/2022
 * @project my_project_trello
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Card extends AbsDomain {
    @Column(nullable = false)
    private String name;
    private String description;

    @OneToMany(mappedBy = "card")
    private List<Commit> commit;

    @ManyToOne
    private Columin columin;

    @ManyToMany
    private List<AuthUser> user;

}
