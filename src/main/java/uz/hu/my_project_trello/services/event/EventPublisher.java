package uz.hu.my_project_trello.services.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author "Husniddin Ulachov"
 * @created 6:14 PM on 9/4/2022
 * @project my_project_trello
 */
@Component
public class EventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    EventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public <T> void publishCustomEvent(final GenericEvent<T> genericEvent) {
        System.out.println("Publishing custom event. ");
        if (genericEvent.success) {
            applicationEventPublisher.publishEvent(genericEvent.getWhat());
        }
    }
}
