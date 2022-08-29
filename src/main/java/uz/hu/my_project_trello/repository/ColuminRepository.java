package uz.hu.my_project_trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.hu.my_project_trello.domains.project.Columin;
import uz.hu.my_project_trello.domains.project.Workspace;

/**
 * @author "Husniddin Ulachov"
 * @created 11:26 AM on 8/25/2022
 * @project my_project_trello
 */
public interface ColuminRepository extends JpaRepository<Columin,Long> {
}
