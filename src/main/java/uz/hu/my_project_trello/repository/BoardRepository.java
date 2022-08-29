package uz.hu.my_project_trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.hu.my_project_trello.domains.project.Board;
import uz.hu.my_project_trello.domains.project.Workspace;

import java.util.List;

/**
 * @author "Husniddin Ulachov"
 * @created 11:26 AM on 8/25/2022
 * @project my_project_trello
 */
public interface BoardRepository extends JpaRepository<Board,Long> {
    @Query(value = "update Board b set b.isDeleted=true where b.createdBy=:ownerId and b.id=:id")
    Boolean softDelete(Long ownerId, Long id);

    @Query(value = "delete from Board b where b.id=:id and b.createdBy=:ownerId")
    Boolean hardDelete(Long ownerId, Long id);

    @Query(value = "select w.boards from Workspace w inner join AuthUser u  where w.id=:wId and (w.createdBy =:id or u.id=:id)")
    List<Board> getAll(Long id,Long wId);

    @Query(value = "select b from Board b inner join AuthUser u where (b.createdBy=:id or u.id=:id) and b.id=:bId")
    Board getOne(Long id, Long bId);
}
