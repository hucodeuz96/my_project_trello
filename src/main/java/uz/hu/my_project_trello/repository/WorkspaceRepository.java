package uz.hu.my_project_trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.hu.my_project_trello.domains.project.Workspace;

import java.util.List;
import java.util.Optional;



/**
 * @author "Husniddin Ulachov"
 * @created 11:26 AM on 8/25/2022
 * @project my_project_trello
 */
public interface

WorkspaceRepository extends JpaRepository<Workspace,Long> {
    @Modifying
    @Query("update Workspace w set w.isDeleted=true where w.id=:id AND w.createdBy=:ownerId")
    Boolean softDeleted(Long id,Long ownerId);

    @Query("delete from Workspace w  where w.id=:id and w.createdBy =:ownerId")
    Boolean hardDeleted(Long id,Long ownerId);

    @Query( "select w  from Workspace w inner join w.user u   where  u.id=:id and w.isDeleted=false")
    List<Workspace> findAllBYID(Long id);

    @Query( "select w  from Workspace w inner join w.user u  where   u.id=:id and w.id=:wId and w.isDeleted=false")
    Workspace findOne(Long id, Long wId);

    @Query("select w from Workspace w inner join w.boards b inner join b.user u where u.id=:id")
    List<Workspace> getAllForBoardUser(Long id);

}
