package uz.hu.my_project_trello.services.project;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.hu.my_project_trello.domains.auth.AuthUser;
import uz.hu.my_project_trello.domains.project.Card;
import uz.hu.my_project_trello.domains.project.Commit;
import uz.hu.my_project_trello.dtos.project.commit.CommitCreateDTO;
import uz.hu.my_project_trello.dtos.project.commit.CommitResDTO;
import uz.hu.my_project_trello.dtos.project.commit.CommitUpdateDTO;
import uz.hu.my_project_trello.exceptions.GenericNotFoundException;
import uz.hu.my_project_trello.mappers.CommitMapper;
import uz.hu.my_project_trello.repository.AuthUserRepository;
import uz.hu.my_project_trello.repository.CardRepository;
import uz.hu.my_project_trello.repository.CommitRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author "Husniddin Ulachov"
 * @created 2:59 PM on 9/2/2022
 * @project my_project_trello
 */
@Service
@RequiredArgsConstructor
public class CommitService extends AbsProjectService<CommitResDTO, CommitUpdateDTO, CommitCreateDTO,Long> {
    private final CommitRepository commitRepository;
    private final CardRepository cardRepository;
    private final AuthUserRepository authUserRepository;
    private final CommitMapper commitMapper;
    public AuthUser getUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
       return authUserRepository.findByUsername(name).orElseThrow(() -> new GenericNotFoundException("not Authorization", 403));
    }

    @Override
    public CommitResDTO generate(CommitCreateDTO commitCreateDTO) {
        Commit commit = new Commit();
        AuthUser authUser = authUserRepository.findById(commitCreateDTO.getReplyId()).orElseThrow(() -> new GenericNotFoundException("User not found", 404));
        commit.setAuthUser(authUser);
        Card card = cardRepository.findById(commitCreateDTO.getCardId()).orElseThrow(() -> new GenericNotFoundException("Not found Card", 404));
        commit.setCard(card);
        commit.setText(commitCreateDTO.getText());
        commit.setCreatedBy(getUser().getId());
        Commit save = commitRepository.save(commit);
        CommitResDTO commitResDTO = commitMapper.fromCommit(save);
        commitResDTO.setCardId(card.getId());
        commitResDTO.setUserId(authUser.getId());
        return  commitResDTO;

    }

    @Override
    public CommitResDTO edit(CommitUpdateDTO commitUpdateDTO) {
        Commit commit = commitRepository.findById(commitUpdateDTO.getId()).orElseThrow(() -> new GenericNotFoundException("Not found Commit", 404));
        if (commit.getCreatedBy().equals(getUser().getId())) {
            commit.setText(commitUpdateDTO.getText());
            commit.setUpdatedAt(LocalDateTime.now());
            Commit save = commitRepository.save(commit);
           return commitMapper.fromCommit(save);
        }
        else {
            throw new GenericNotFoundException("You can't rewrite on this commit",403);
        }
    }

    @Override
    public CommitResDTO getOne(Long aLong) {
        return null;
    }

    @Override
    public List<CommitResDTO> getAll(Long id) {
        List<CommitResDTO>commitResDTOS = new ArrayList<>();
        cardRepository.findById(id).orElseThrow(() -> new GenericNotFoundException("not found card",404));
        for (Commit commit : commitRepository.getAllByCardID(id)) {
            CommitResDTO commitResDTO = commitMapper.fromCommit(commit);
            commitResDTOS.add(commitResDTO);
        }
        return commitResDTOS;
    }

    @Override
    public void softDelete(Long id) {
        Commit commit = commitRepository.findById(id).orElseThrow(() -> new GenericNotFoundException("Not found commit", 404));
        if (commit.getCreatedBy().equals(getUser().getId())){
            commitRepository.softDeleted(id);
        }
    }

    @Override
    public void hardDelete(Long id) {
        Commit commit = commitRepository.findById(id).orElseThrow(() -> new GenericNotFoundException("Not found commit", 404));
        if (commit.getCreatedBy().equals(getUser().getId())){
            commitRepository.deleteById(id);
        }
    }
}
