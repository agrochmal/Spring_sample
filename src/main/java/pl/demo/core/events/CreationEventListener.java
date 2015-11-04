package pl.demo.core.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.User;

/**
 * Created by robertsikora on 22.10.15.
 */

@Component
public class CreationEventListener {

    //TO-DO send mails

    @EventListener
    public void handleCreationEventAdvert(final CreationEvent<Advert> creationEvent) {
        if(creationEvent.getSource() instanceof Advert) {
            System.out.println("Just created new advert. Title: " + creationEvent.getSource().getTitle());
        }
    }

    @EventListener
    public void handleCreationEventUser(final CreationEvent<User> creationEvent) {
        if(creationEvent.getSource() instanceof User) {
            System.out.println("Just created new user. Name: " + creationEvent.getSource().getName());
        }
    }
}
