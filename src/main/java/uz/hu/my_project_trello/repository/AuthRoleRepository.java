package uz.hu.my_project_trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.hu.my_project_trello.domains.auth.AuthRole;


public interface AuthRoleRepository extends JpaRepository<AuthRole, Long> {
}
