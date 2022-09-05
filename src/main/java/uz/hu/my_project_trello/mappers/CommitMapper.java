package uz.hu.my_project_trello.mappers;

import org.mapstruct.Mapper;
import uz.hu.my_project_trello.domains.project.Comment;
import uz.hu.my_project_trello.dtos.project.comment.CommentResDTO;

/**
 * @author "Husniddin Ulachov"
 * @created 5:03 PM on 9/2/2022
 * @project my_project_trello
 */
@Mapper(componentModel = "spring")
public interface CommitMapper {

    CommentResDTO fromCommit(Comment comment);
}
