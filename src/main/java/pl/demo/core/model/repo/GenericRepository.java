package pl.demo.core.model.repo;

import pl.demo.core.model.entity.BaseEntity;

/**
 * Created by robertsikora on 26.07.15.
 */
public interface GenericRepository<T extends BaseEntity> {

    void detach(T entity);
}
