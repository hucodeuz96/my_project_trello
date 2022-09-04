package uz.hu.my_project_trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.hu.my_project_trello.domains.project.Card;
import uz.hu.my_project_trello.domains.project.Workspace;

import java.util.List;
import java.util.Optional;

/**
 * @author "Husniddin Ulachov"
 * @created 11:26 AM on 8/25/2022
 * @project my_project_trello
 */
public interface CardRepository extends JpaRepository<Card,Long> {

    @Query("select c from Card c inner join c.user u where c.isDeleted=false and c.id=:id and u.id=:uId")
    Optional<Card> getOneByID(Long id,Long uId);

    @Query("select c from Card c inner join c.columin col where c.isDeleted = false and col.id=:id")
    List<Card> getAllByColumnId (Long id);

    @Query("update Card c set c.isDeleted=true where c.id=:id")
    void softDeleteByCardId(Long id);

}
