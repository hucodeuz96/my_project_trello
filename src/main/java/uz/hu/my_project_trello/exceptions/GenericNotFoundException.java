package uz.hu.my_project_trello.exceptions;


public class GenericNotFoundException extends GenericRuntimeException {
    public GenericNotFoundException(String message, Integer statusCode) {
        super(message, statusCode);
    }
}
