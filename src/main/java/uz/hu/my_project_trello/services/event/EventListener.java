package uz.hu.my_project_trello.services.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.hu.my_project_trello.domains.project.Board;
import uz.hu.my_project_trello.domains.project.Card;
import uz.hu.my_project_trello.domains.project.Columin;
import uz.hu.my_project_trello.dtos.project.card.CardMoveDTO;
import uz.hu.my_project_trello.dtos.project.columin.ColumnMoveDTO;
import uz.hu.my_project_trello.exceptions.GenericNotFoundException;
import uz.hu.my_project_trello.repository.BoardRepository;
import uz.hu.my_project_trello.repository.CardRepository;
import uz.hu.my_project_trello.repository.ColuminRepository;
import uz.hu.my_project_trello.services.auth.GetSessionUser;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author "Husniddin Ulachov"
 * @created 6:11 PM on 9/4/2022
 * @project my_project_trello
 */
@Component
@RequiredArgsConstructor
public class EventListener {
    private final BoardRepository boardRepository;
    private final GetSessionUser getSessionUser;
    private final CardRepository cardRepository;
    private final ColuminRepository columinRepository;
    @org.springframework.context.event.EventListener
    public void moveCardToColumin(@NotNull CardMoveDTO cardMoveDTO){
        Card card = cardRepository.findById(cardMoveDTO.getCardId()).orElseThrow(() -> new GenericNotFoundException("Not found card", 404));
        Columin columin = columinRepository.findById(cardMoveDTO.getColuminId()).orElseThrow(() -> new GenericNotFoundException("not found column", 404));
        columin.getCard().forEach(card1 -> {
            if (card1.getId().equals(card.getId())){
                card.setId(null);
            }
        });
        card.setColumin(columin);
        card.setUpdatedAt(LocalDateTime.now());
        card.setUpdatedBy(getSessionUser.getUser().getId());
        cardRepository.save(card);
    }
    @org.springframework.context.event.EventListener
    public void moveColumn(@NotNull ColumnMoveDTO columnMoveDTO){
        columinRepository.findById(columnMoveDTO.getFromColumnId()).orElseThrow(() -> new GenericNotFoundException("Not found Columin", 404));
        columinRepository.findById(columnMoveDTO.getToColumnId()).orElseThrow(() -> new GenericNotFoundException("Not found Columin", 404));
        boardRepository.findById(columnMoveDTO.getBoardId()).orElseThrow(() -> new GenericNotFoundException("Not found Board",404));
        if (columnMoveDTO.getFromColumnId()<columnMoveDTO.getToColumnId()){
            columinRepository.moveColumnToRight(Math.toIntExact(columnMoveDTO.getFromColumnId()), Math.toIntExact(columnMoveDTO.getToColumnId()),columnMoveDTO.getBoardId());
        }
        else {
            columinRepository.moveColumnToLeft(Math.toIntExact(columnMoveDTO.getFromColumnId()), Math.toIntExact(columnMoveDTO.getToColumnId()),columnMoveDTO.getBoardId());
        }
    }

}
