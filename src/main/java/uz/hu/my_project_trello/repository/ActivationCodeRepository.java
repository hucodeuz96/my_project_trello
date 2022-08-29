package uz.hu.my_project_trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.hu.my_project_trello.domains.auth.ActivationCode;


import java.util.Optional;


public interface ActivationCodeRepository extends JpaRepository<ActivationCode, Long> {
    Optional<ActivationCode> findByActivationLink(String link);
}
