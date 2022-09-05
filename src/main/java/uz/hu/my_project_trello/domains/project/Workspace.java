package uz.hu.my_project_trello.domains.project;

import lombok.*;
import uz.hu.my_project_trello.domains.auth.AuthUser;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;

/**
 * @author "Husniddin Ulachov"
 * @created 4:09 PM on 8/24/2022
 * @project my_project_trello
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Workspace extends AbsDomain {
    @Column(nullable = false,unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkspaceType workspaceType;

    @Getter
    @AllArgsConstructor
    public enum WorkspaceType {
        MARKETING,
        EDUCATION,
        OPERATIONS,
        OTHER
    }

    private String description;

    @Enumerated(value = EnumType.STRING)
    private Visibility visibility;
    public enum Visibility{
        PUBLIC,
        PRIVATE,
        WORKCSPACE
    }

    @ManyToMany
    private List<AuthUser> user;

    @OneToMany(mappedBy = "workspace",cascade = CascadeType.REMOVE)
    private List<Board> boards;
}
