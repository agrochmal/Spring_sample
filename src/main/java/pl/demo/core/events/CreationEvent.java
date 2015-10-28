package pl.demo.core.events;

/**
 * Created by robertsikora on 22.10.15.
 */
public interface CreationEvent<T> {
    T getSource();
}
