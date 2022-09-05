package uz.hu.my_project_trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.hu.my_project_trello.domains.project.Comment;

import java.util.List;

/**
 * @author "Husniddin Ulachov"
 * @created 11:26 AM on 8/25/2022
 * @project my_project_trello
 */
public interface CommitRepository extends JpaRepository<Comment,Long> {
    @Query("select c from Comment c inner join c.card cd  where cd.id=:id and c.isDeleted =false")
    List<Comment> getAllByCardID(Long id);

    @Query("update Comment c set c.isDeleted=true where c.id=:id")
    void softDeleted (Long id);

}
