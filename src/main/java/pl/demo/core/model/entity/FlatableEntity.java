package pl.demo.core.model.entity;

/**
 * Created by robertsikora on 26.07.15.
 */
public interface FlatableEntity {

    /**
     * Method create flat DTO object from entity.
     * Set null value on each fields which have any relation to target entity.
     */
    void flatEntity();
}
