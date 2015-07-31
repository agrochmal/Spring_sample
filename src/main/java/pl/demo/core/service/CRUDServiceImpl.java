package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.demo.core.model.entity.BaseEntity;
import pl.demo.core.model.entity.FlatableEntity;
import pl.demo.core.model.repo.GenericRepository;

import java.io.Serializable;

/**
 * Created by robertsikora on 27.07.15.
 */
public abstract class CRUDServiceImpl<PK extends Serializable, E extends BaseEntity> implements CRUDService<PK, E>{

    private final JpaRepository<E, PK> jpaRepository;
    private GenericRepository<E> genericRepository;

    public CRUDServiceImpl(final JpaRepository<E, PK> jpaRepository){
        this.jpaRepository = jpaRepository;
    }

    @Override
    public E findOne(PK id) {
        Assert.notNull(id, "Entity id is required");
        final E entity = getJpaRepository().findOne(id);
        Assert.state(null != entity, "Entity doesn't exist in db");
        getGenericRepository().detach(entity);
        Assert.state(entity instanceof FlatableEntity, "Entity must implement FlatableEntity interface!");
        ((FlatableEntity)entity).flatEntity();
        return entity;
    }

    @Override
    @Transactional(readOnly=false)
    public void delete(PK id) {
        Assert.notNull(id, "Entity id is required");
        getJpaRepository().delete(id);
    }

    @Override
    @Transactional(readOnly=false)
    public void edit(E entity) {
    }

    @Override
    @Transactional(readOnly=false)
    public E save(E entity) {
        Assert.notNull(entity, "Entity is required");
        return getJpaRepository().save(entity);
    }

    protected JpaRepository<E, PK> getJpaRepository() {
        return jpaRepository;
    }

    protected GenericRepository<E> getGenericRepository() {
        return genericRepository;
    }

    @Autowired
    public void setGenericRepository(GenericRepository<E> genericRepository) {
        this.genericRepository = genericRepository;
    }
}
