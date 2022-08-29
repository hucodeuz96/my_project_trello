package uz.hu.my_project_trello.exceptions;

import lombok.Getter;



@Getter
public class GenericInvalidTokenException extends GenericNotFoundException {
    public GenericInvalidTokenException(String message, Integer statusCode) {
        super(message, statusCode);
    }
}
