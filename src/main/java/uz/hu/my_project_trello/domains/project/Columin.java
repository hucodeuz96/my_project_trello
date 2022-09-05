package uz.hu.my_project_trello.domains.project;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * @author "Husniddin Ulachov"
 * @created 4:52 PM on 8/24/2022
 * @project my_project_trello
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Columin extends AbsDomain {
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "columin")
    private List<Card> card;
    @ManyToOne
    private Board board;
    private Long orderNumber;
}
