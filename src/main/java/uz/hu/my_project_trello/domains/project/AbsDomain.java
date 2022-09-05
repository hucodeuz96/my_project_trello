package uz.hu.my_project_trello.domains.project;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author "Husniddin Ulachov"
 * @created 9:22 AM on 8/25/2022
 * @project my_project_trello
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract  class AbsDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt = LocalDateTime.now() ;
    private LocalDateTime updatedAt;

    private Long createdBy;
    private Long updatedBy;

    private boolean isDeleted = false;

}
