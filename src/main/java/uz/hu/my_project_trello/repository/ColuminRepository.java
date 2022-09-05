package uz.hu.my_project_trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import uz.hu.my_project_trello.domains.project.Columin;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author "Husniddin Ulachov"
 * @created 11:26 AM on 8/25/2022
 * @project my_project_trello
 */
public interface ColuminRepository extends JpaRepository<Columin,Long> {

    @Query(value = "select c from Columin c inner join c.board b where c.isDeleted=false and b.id =:bId")
    List<Columin> getAllByBoardID(Long bId);

    @Query(value = "select c from Columin c  where c.isDeleted=false and c.id=:id")
    Optional<Columin> getOneByColuminID(Long id);

    @Modifying
    @Query(value = "update Columin c set c.isDeleted=true where c.id=:id")
    void softDeleteByColumnID(Long id);

    @Modifying
    @Transactional
    @Query("update Columin c set c.orderNumber = c.orderNumber + 1 where c.orderNumber >= :from and c.orderNumber < :to and c.board.id=:boardId")
    void moveColumnToLeft(int from, int to, Long boardId);

    @Modifying
    @Transactional
    @Query("update Columin c set c.orderNumber = c.orderNumber - 1 where c.orderNumber <= :to and c.orderNumber > :from and c.board.id=:boardId")
    void moveColumnToRight(int from, int to, Long boardId);










}
