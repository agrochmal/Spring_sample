package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.Assert;
import pl.demo.core.model.entity.BaseEntity;
import pl.demo.core.model.entity.FlatableEntity;
import pl.demo.core.model.repo.GenericRepository;

import java.io.Serializable;

/**
 * Created by robertsikora on 27.07.15.
 */
public abstract class CRUDServiceImpl<PK extends Serializable, E extends BaseEntity> implements CRUDService<PK, E>{

    private JpaRepository<E, PK> jpaRepository;
    private GenericRepository<E> genericRepository;

    public CRUDServiceImpl(final JpaRepository<E, PK> jpaRepository){
        this.jpaRepository = jpaRepository;
    }

    @Override
    public E findOne(PK id) {
        Assert.notNull(id, "Entity id is required");
        final E entity = getJpaRepository().findOne(id);
        Assert.state(null != entity, "Advert doesn't exist in db");
        getGenericRepository().detach(entity);
        Assert.state(entity instanceof FlatableEntity, "Entity must implement FlatableEntity interface!");
        ((FlatableEntity)entity).flatEntity();
        return entity;
    }

    @Override
    public void delete(PK id) {
        Assert.notNull(id, "Entity id is required");
        getJpaRepository().delete(id);
    }

    @Override
    public void edit(E entity) {
    }

    @Override
    public E save(E entity) {
        Assert.notNull(entity, "Entity is required");
        return getJpaRepository().save(entity);
    }


    public JpaRepository<E, PK> getJpaRepository() {
        return jpaRepository;
    }

    public void setJpaRepository(JpaRepository<E, PK> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public GenericRepository<E> getGenericRepository() {
        return genericRepository;
    }

    @Autowired
    public void setGenericRepository(GenericRepository<E> genericRepository) {
        this.genericRepository = genericRepository;
    }
}
