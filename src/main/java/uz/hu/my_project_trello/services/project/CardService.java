package uz.hu.my_project_trello.services.project;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.hu.my_project_trello.domains.auth.AuthUser;
import uz.hu.my_project_trello.domains.project.Card;
import uz.hu.my_project_trello.domains.project.Columin;
import uz.hu.my_project_trello.dtos.project.*;
import uz.hu.my_project_trello.dtos.project.card.CardCreateDTO;
import uz.hu.my_project_trello.dtos.project.card.CardMoveDTO;
import uz.hu.my_project_trello.dtos.project.card.CardResDTO;
import uz.hu.my_project_trello.dtos.project.card.CardUpdateDTO;
import uz.hu.my_project_trello.exceptions.GenericNotFoundException;
import uz.hu.my_project_trello.mappers.CardMapper;
import uz.hu.my_project_trello.repository.AuthUserRepository;
import uz.hu.my_project_trello.repository.BoardRepository;
import uz.hu.my_project_trello.repository.CardRepository;
import uz.hu.my_project_trello.repository.ColuminRepository;
import uz.hu.my_project_trello.services.auth.GetSessionUser;
import uz.hu.my_project_trello.services.event.EventPublisher;
import uz.hu.my_project_trello.services.event.GenericEvent;

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
    private final BoardRepository boardRepository;
    private final EventPublisher publisher;
    private final GetSessionUser session;
    @Override
    public CardResDTO generate(CardCreateDTO cardCreateDTO) {
        Columin columin = columinRepository.findById(cardCreateDTO.getColuminId()).orElseThrow(() -> new GenericNotFoundException("Not found Columin", 404));
        boardRepository.getOneBYId(session.getUser().getId(),columin.getBoard().getId()).orElseThrow(() -> new GenericNotFoundException("Server refused you to visit board",403));
        Card card = new Card();
        card.setName(cardCreateDTO.getName());
        card.setColumin(columin);
        card.setCreatedBy(session.getUser().getId());
        card.setUser(List.of(session.getUser()));
        Card save = cardRepository.save(card);
        CardResDTO cardResDTO = cardMapper.fromCard(save);
        cardResDTO.setColuminId(columin.getId());
        return cardResDTO;
    }
    @Override
    public CardResDTO edit(CardUpdateDTO cardUpdateDTO) {
        Columin columin = columinRepository.findById(cardUpdateDTO.getColuminId()).orElseThrow(() -> new GenericNotFoundException("Not found Columin", 404));
        boardRepository.getOneBYId(session.getUser().getId(),columin.getBoard().getId()).orElseThrow(() -> new GenericNotFoundException("Server refused you to visit board",403));
        Card card = cardRepository.getOneByID(cardUpdateDTO.getId(),session.getUser().getId()).orElseThrow(() -> new GenericNotFoundException("not found card", 404));
        card.setName(cardUpdateDTO.getName());
        card.setDescription(cardUpdateDTO.getDescription());
        card.setUpdatedBy(session.getUser().getId());
        card.setUpdatedAt(LocalDateTime.now());
        Card save = cardRepository.save(card);
        return cardMapper.fromCard(save);
    }

    @Override
    public CardResDTO getOne(Long id) {
        Columin columin = columinRepository.findById(id).orElseThrow(() -> new GenericNotFoundException("Not found Columin", 404));
        boardRepository.getOneBYId(session.getUser().getId(),columin.getBoard().getId()).orElseThrow(() -> new GenericNotFoundException("Server refused you to visit board",403));
        Card card = cardRepository.getOneByID(id,session.getUser().getId()).orElseThrow(() -> new GenericNotFoundException("not found crad", 404));
        return cardMapper.fromCard(card);
    }

    @Override
    public List<CardResDTO> getAll(Long id) {
        Columin columin = columinRepository.findById(id).orElseThrow(() -> new GenericNotFoundException("Not found Columin", 404));
        boardRepository.getOneBYId(session.getUser().getId(),columin.getBoard().getId()).orElseThrow(() -> new GenericNotFoundException("Server refused you to visit board",403));
        List<CardResDTO> cardResDTOS = new ArrayList<>();
        for (Card card : cardRepository.getAllByColumnId(id)) {
            CardResDTO cardResDTO = cardMapper.fromCard(card);
            cardResDTOS.add(cardResDTO);
        }
        return cardResDTOS;
    }

    @Override
    public void softDelete(Long id) {
        cardRepository.softDeleteByCardId(id);
    }

    @Override
    public void hardDelete(Long id) {
        cardRepository.deleteById(id);
    }

    public void addUser(AddMemberDTO addMemberDTO) {
        Card card = cardRepository.getOneByID(addMemberDTO.getSpaceId(),session.getUser().getId()).orElseThrow(() -> new GenericNotFoundException("Not found Card", 404));
        boardRepository.getOneBYId(session.getUser().getId(),card.getColumin().getBoard().getId()).orElseThrow(() -> new GenericNotFoundException("Server refused you to visit board",403));
        List<AuthUser> users = new ArrayList<>();
        Card found = cardRepository.findById(addMemberDTO.getSpaceId()).orElseThrow(() -> new GenericNotFoundException("Not found", 404));
        addMemberDTO.getUserList().forEach(id -> {
            AuthUser authUser = authUserRepository.findById(id).orElseThrow(() -> new GenericNotFoundException("Member not found", 404));
            users.add(authUser);
        } );
        users.addAll(found.getUser());
        found.setUser(users);
        cardRepository.save(found);
    }
    public List<AuthUser> getUserBYCardId(Long id){
        Card card = cardRepository.findById(id).orElseThrow(() -> new GenericNotFoundException("Not Found card", 404));
        return card.getUser();
    }

    public void moveCardTo(CardMoveDTO cartMoveDTO) {
        GenericEvent<CardMoveDTO> genericEvent = new GenericEvent<>(cartMoveDTO, true);
        publisher.publishCustomEvent(genericEvent);
    }
}
