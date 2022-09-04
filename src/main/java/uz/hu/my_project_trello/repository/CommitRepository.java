package uz.hu.my_project_trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.hu.my_project_trello.domains.project.Commit;
import uz.hu.my_project_trello.domains.project.Workspace;

import java.util.List;

/**
 * @author "Husniddin Ulachov"
 * @created 11:26 AM on 8/25/2022
 * @project my_project_trello
 */
public interface CommitRepository extends JpaRepository<Commit,Long> {
    @Query("select c from Commit c inner join c.card cd inner join cd.user u where cd.id=:id and c.isDeleted =false " +
            "and (cd.createdBy=:id or u.id=:id)")
    List<Commit> getAllByCardID(Long id);

    @Query("update Commit c set c.isDeleted=true where c.id=:id")
    void softDeleted (Long id);

}
