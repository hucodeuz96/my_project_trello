package uz.hu.my_project_trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import uz.hu.my_project_trello.domains.project.Board;
import uz.hu.my_project_trello.domains.project.Workspace;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author "Husniddin Ulachov"
 * @created 11:26 AM on 8/25/2022
 * @project my_project_trello
 */
public interface BoardRepository extends JpaRepository<Board,Long> {
    @Transactional
    @Modifying
    @Query(value = "update Board b  set b.isDeleted = true where b.createdBy=:ownerId and b.id=:id")
    void softDelete(Long ownerId, Long id);

    @Transactional
    @Modifying
    @Query(value = "delete from Board b where b.id=:id and b.createdBy=:ownerId")
    void hardDelete(Long ownerId, Long id);

    @Query(value = "select w.boards from Workspace w inner join w.user u  where w.id=:wId and w.isDeleted=false and (w.createdBy =:id or u.id=:id)")
    List<Board> getAll(Long id,Long wId);

    @Query(value = "select b from Board b inner join b.user u where b.id=:id and b.isDeleted=false and (b.createdBy=:userId or u.id=:userId)")
    Board getOneBYId(Long userId, Long id);
}
