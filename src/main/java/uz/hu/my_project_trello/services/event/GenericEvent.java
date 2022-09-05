package uz.hu.my_project_trello.services.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * @author "Husniddin Ulachov"
 * @created 6:16 PM on 9/4/2022
 * @project my_project_trello
 */
@Getter
@Setter
public class GenericEvent <T> extends ApplicationEvent {
    private T what;
    protected boolean success;

    public GenericEvent(T what,boolean success) {
        super(what);
        this.what= what;
        this.success = success;

    }
}
