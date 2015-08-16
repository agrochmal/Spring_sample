package pl.demo.core.model.repo;

import org.springframework.stereotype.Repository;
import pl.demo.core.model.entity.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by robertsikora on 26.07.15.
 */

@Repository
public class GenericRepositoryImpl<T extends BaseEntity> implements GenericRepository<T>{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void detach(final T entity) {
        entityManager.detach(entity);
    }
}
