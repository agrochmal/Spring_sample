package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.demo.core.model.entity.BaseEntity;
import pl.demo.core.model.entity.FlatableEntity;
import pl.demo.core.model.repo.GenericRepository;

import java.io.Serializable;
import java.util.Collection;

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
    public E findOne(final PK id) {
        Assert.notNull(id, "Entity id is required");
        final E entity = getJpaRepository().findOne(id);
        Assert.state(null != entity, "Entity doesn't exist in db");
        getGenericRepository().detach(entity);
        Assert.state(entity instanceof FlatableEntity, "Entity must implement FlatableEntity interface!");
        entity.flatEntity();
        return entity;
    }

    @Override
    public Collection<E> findAll(){
        final Collection<E> entities = getJpaRepository().findAll();
        unproxyEntity(entities);
        return entities;
    }

    @Override
    @Transactional(readOnly=false)
    public void delete(final PK id) {
        Assert.notNull(id, "Entity id is required");
        getJpaRepository().delete(id);
        getJpaRepository().flush();
    }

    @Override
    @Transactional(readOnly=false)
    public void edit(final PK id, final E entity) {
        throw new IllegalArgumentException("Method not supported yet!");
    }

    @Override
    @Transactional(readOnly=false)
    public E save(final E entity) {
        Assert.notNull(entity, "Entity is required");
        final E result = getJpaRepository().save(entity);
        getJpaRepository().flush();
        return result;
    }

    protected void unproxyEntity(final E...entities){
        for(final E entity : entities) {
            Assert.isTrue(entity instanceof FlatableEntity);
            getGenericRepository().detach(entity);
            entity.flatEntity();
        }
    }

    protected void unproxyEntity(final Collection<E> collection){
        for(final E entity : collection) {
            Assert.isTrue(entity instanceof FlatableEntity);
            getGenericRepository().detach(entity);
            entity.flatEntity();
        }
    }

    protected JpaRepository<E, PK> getJpaRepository() {
        return jpaRepository;
    }

    private GenericRepository<E> getGenericRepository() {
        return genericRepository;
    }

    @Autowired
    public void setGenericRepository(GenericRepository<E> genericRepository) {
        this.genericRepository = genericRepository;
    }
}
