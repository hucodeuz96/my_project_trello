package uz.hu.my_project_trello.services.project;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.hu.my_project_trello.domains.auth.AuthUser;
import uz.hu.my_project_trello.domains.project.Card;
import uz.hu.my_project_trello.domains.project.Comment;
import uz.hu.my_project_trello.dtos.project.comment.CommentCreateDTO;
import uz.hu.my_project_trello.dtos.project.comment.CommentResDTO;
import uz.hu.my_project_trello.dtos.project.comment.CommentUpdateDTO;
import uz.hu.my_project_trello.exceptions.GenericNotFoundException;
import uz.hu.my_project_trello.mappers.CommitMapper;
import uz.hu.my_project_trello.repository.AuthUserRepository;
import uz.hu.my_project_trello.repository.BoardRepository;
import uz.hu.my_project_trello.repository.CardRepository;
import uz.hu.my_project_trello.repository.CommitRepository;
import uz.hu.my_project_trello.services.auth.GetSessionUser;

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
public class CommentService extends AbsProjectService<CommentResDTO, CommentUpdateDTO, CommentCreateDTO,Long> {
    private final CommitRepository commitRepository;
    private final CardRepository cardRepository;
    private final AuthUserRepository authUserRepository;
    private final CommitMapper commitMapper;
    private  final BoardRepository boardRepository;

    private final GetSessionUser session;

    @Override
    public CommentResDTO generate(CommentCreateDTO commitCreateDTO) {
        Comment comment = new Comment();
        Card card = cardRepository.findById(commitCreateDTO.getCardId()).orElseThrow(() -> new GenericNotFoundException("Not found Card", 404));
        comment.setCard(card);
        comment.setText(commitCreateDTO.getText());
        comment.setCreatedBy(session.getUser().getId());
        comment.setAuthUser(session.getUser());
        Comment save = commitRepository.save(comment);
        CommentResDTO commentResDTO = commitMapper.fromCommit(save);
        commentResDTO.setCardId(card.getId());
        commentResDTO.setUserId(session.getUser().getId());
        return commentResDTO;

    }

    @Override
    public CommentResDTO edit(CommentUpdateDTO commentUpdateDTO) {
        Comment comment = commitRepository.findById(commentUpdateDTO.getId()).orElseThrow(() -> new GenericNotFoundException("Not found Commit", 404));
        if (comment.getCreatedBy().equals(session.getUser().getId())) {
            comment.setText(commentUpdateDTO.getText());
            comment.setUpdatedAt(LocalDateTime.now());
            Comment save = commitRepository.save(comment);
           return commitMapper.fromCommit(save);
        }
        else {
            throw new GenericNotFoundException("You can't rewrite on this commit",403);
        }
    }

    @Override
    public CommentResDTO getOne(Long aLong) {
        return null;
    }

    @Override
    public List<CommentResDTO> getAll(Long id) {
        List<CommentResDTO> commentResDTOS = new ArrayList<>();
        Card card = cardRepository.findById(id).orElseThrow(() -> new GenericNotFoundException("not found card", 404));
        boardRepository.getOneBYId(session.getUser().getId(),card.getColumin().getBoard().getId());
        commitRepository.getAllByCardID(id).forEach(comment -> {
            CommentResDTO commentResDTO = commitMapper.fromCommit(comment);
            commentResDTO.setUserId(comment.getAuthUser().getId());
            commentResDTO.setCardId(comment.getCard().getId());
            commentResDTOS.add(commentResDTO);
        });
        return commentResDTOS;
    }
    @Override
    public void softDelete(Long id) {
        Comment comment = commitRepository.findById(id).orElseThrow(() -> new GenericNotFoundException("Not found commit", 404));
        if (comment.getCreatedBy().equals(session.getUser().getId())){
            commitRepository.softDeleted(id);
        }
    }

    @Override
    public void hardDelete(Long id) {
        Comment comment = commitRepository.findById(id).orElseThrow(() -> new GenericNotFoundException("Not found commit", 404));
        if (comment.getCreatedBy().equals(session.getUser().getId())){
            commitRepository.deleteById(id);
        }
    }
}
