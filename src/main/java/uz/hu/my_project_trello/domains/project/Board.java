package uz.hu.my_project_trello.domains.project;

import lombok.*;
import uz.hu.my_project_trello.domains.auth.AuthUser;


import javax.persistence.*;
import java.util.List;

/**
 * @author "Husniddin Ulachov"
 * @created 4:33 PM on 8/24/2022
 * @project my_project_trello
 */

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Board extends AbsDomain {
    @Column(nullable = false)
    private String name;
    @ManyToOne
    private Workspace workspace;

    @OneToMany(mappedBy = "board")
    private List<Columin> columns;

    @ManyToMany
    private List<AuthUser> user;

    @Enumerated(EnumType.STRING)
    private BoardVisibility visibility;

    public enum BoardVisibility {
        PRIVATE,
        WORKSPACE,
        PUBLIC
    }




}
