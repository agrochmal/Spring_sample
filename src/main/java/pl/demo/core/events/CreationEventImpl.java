package pl.demo.core.events;

import org.springframework.context.ApplicationEvent;

/**
 * Created by robertsikora on 22.10.15.
 */

public class CreationEventImpl<T> extends ApplicationEvent
        implements CreationEvent<T> {

    public CreationEventImpl(final T source){
        super(source);
    }

    public T getSource(){
        return (T)super.getSource();
    }
}
