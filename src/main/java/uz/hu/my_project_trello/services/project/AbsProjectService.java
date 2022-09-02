package uz.hu.my_project_trello.services.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.hu.my_project_trello.domains.auth.AuthUser;
import uz.hu.my_project_trello.exceptions.GenericNotFoundException;
import uz.hu.my_project_trello.repository.AuthUserRepository;

import java.util.List;

/**
 * @author "Husniddin Ulachov"
 * @created 5:06 PM on 8/26/2022
 * @project my_project_trello
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class AbsProjectService<T,D,K,L> {

     public abstract T generate(K k);
     public abstract T edit(D k);
     public abstract T getOne(L l);
     public abstract List<T> getAll(L l);
     public abstract void softDelete(L k);
     public abstract void hardDelete(L k);




}
