package uz.hu.my_project_trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.hu.my_project_trello.domains.project.Columin;
import uz.hu.my_project_trello.domains.project.Workspace;

import java.util.List;

/**
 * @author "Husniddin Ulachov"
 * @created 11:26 AM on 8/25/2022
 * @project my_project_trello
 */
public interface ColuminRepository extends JpaRepository<Columin,Long> {
    @Query("select c from Columin c inner join c.board b where c.isDeleted=false and b.id =:bId ")
    List<Columin> getAll(Long id);
    @Query("select c from Columin c  where c.isDeleted=false and c.id = :id")
    Columin getOne(Long id);

    @Query("update Columin c set c.isDeleted=true where c.id=:id")
    void softDelete(Long id);


}
