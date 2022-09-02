package uz.hu.my_project_trello.services.project;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.hu.my_project_trello.domains.auth.AuthUser;
import uz.hu.my_project_trello.domains.project.Board;
import uz.hu.my_project_trello.domains.project.Card;
import uz.hu.my_project_trello.domains.project.Columin;
import uz.hu.my_project_trello.domains.project.Workspace;
import uz.hu.my_project_trello.dtos.project.*;
import uz.hu.my_project_trello.exceptions.GenericNotFoundException;
import uz.hu.my_project_trello.mappers.CardMapper;
import uz.hu.my_project_trello.mappers.ColuminMapper;
import uz.hu.my_project_trello.repository.AuthUserRepository;
import uz.hu.my_project_trello.repository.BoardRepository;
import uz.hu.my_project_trello.repository.CardRepository;
import uz.hu.my_project_trello.repository.ColuminRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author "Husniddin Ulachov"
 * @created 6:45 PM on 8/30/2022
 * @project my_project_trello
 */
@Service
@RequiredArgsConstructor
public class CardService extends AbsProjectService<CardResDTO, CardUpdateDTO, CardCreateDTO,Long> {
    private final CardRepository cardRepository;
    private final ColuminRepository columinRepository;
    private final AuthUserRepository authUserRepository;
    private final CardMapper cardMapper;

    public Long getSessionId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        AuthUser authUser = authUserRepository.findByUsername(name).orElseThrow(() -> new GenericNotFoundException("not Authorization", 403));
        return authUser.getId();
    }
    @Override
    public CardResDTO generate(CardCreateDTO cardCreateDTO) {
        Columin columin = columinRepository.findById(cardCreateDTO.getColumin()).orElseThrow(() -> new GenericNotFoundException("Not found Columin", 404));
        Card card = new Card();
        card.setName(cardCreateDTO.getName());
        card.setColumin(columin);
        card.setCreatedBy(getSessionId());
        Card save = cardRepository.save(card);
        return cardMapper.fromCard(save);
    }
    @Override
    public CardResDTO edit(CardUpdateDTO cardUpdateDTO) {
        Card card = cardRepository.findById(cardUpdateDTO.getId()).orElseThrow(() -> new GenericNotFoundException("not found crad", 404));
        card.setName(cardUpdateDTO.getName());
        card.setDescription(cardUpdateDTO.getDescription());
        card.setUpdatedBy(getSessionId());
        card.setUpdatedAt(LocalDateTime.now());
        Card save = cardRepository.save(card);
        return cardMapper.fromCard(save);
    }

    @Override
    public CardResDTO getOne(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new GenericNotFoundException("not found crad", 404));
        return cardMapper.fromCard(card);
    }

    @Override
    public List<CardResDTO> getAll(Long id) {
        List<CardResDTO> cardResDTOS = new ArrayList<>();
        for (Card card : cardRepository.getAllBYID(id)) {
            CardResDTO cardResDTO = cardMapper.fromCard(card);
            cardResDTOS.add(cardResDTO);
        }
        return cardResDTOS;
    }

    @Override
    public void softDelete(Long id) {
        cardRepository.softDelete(id);
    }

    @Override
    public void hardDelete(Long id) {
        cardRepository.deleteById(id);
    }

    public void addUser(AddMemberDTO addMemberDTO) {
        List<AuthUser> users = new ArrayList<>();
        Card found = cardRepository.findById(addMemberDTO.getSpaceId()).orElseThrow(() -> new GenericNotFoundException("Not found", 404));
        if (!found.getCreatedBy().equals(getSessionId())){
            throw new GenericNotFoundException("you aren't owner this workspace",403);
        }
        addMemberDTO.getUserList().forEach(id -> {
            AuthUser authUser = authUserRepository.findById(id).orElseThrow(() -> new GenericNotFoundException("Member not found", 404));
            users.add(authUser);
        } );
        users.addAll(found.getUser());
        found.setUser(users);
        cardRepository.save(found);
    }

}
