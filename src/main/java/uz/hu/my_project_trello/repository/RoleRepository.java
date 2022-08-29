package uz.hu.my_project_trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.hu.my_project_trello.domains.auth.AuthRole;
import uz.hu.my_project_trello.domains.auth.AuthUser;

import java.util.List;
import java.util.Optional;

/**
 * @author "Husniddin Ulachov"
 * @created 7:29 PM on 8/23/2022
 * @project my_project_templete
 */
public interface RoleRepository extends JpaRepository<AuthRole,Long> {
    List<AuthRole> findByName(String name);

}
