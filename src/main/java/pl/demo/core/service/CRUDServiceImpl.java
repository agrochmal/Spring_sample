package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.demo.core.model.entity.BaseEntity;
import pl.demo.core.model.entity.FlatableEntity;
import pl.demo.core.model.repo.GenericRepository;
import pl.demo.web.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * Created by robertsikora on 27.07.15.
 */


public abstract class CRUDServiceImpl<PK extends Serializable, E extends BaseEntity>
        implements CRUDService<PK, E>{

    private Map<Class<? extends BaseEntity>, JpaRepository<E, PK>> repositoryMap;
    private GenericRepository<E> genericRepository;

    protected abstract Class<E> supportedDomainClass();

    protected JpaRepository<E, PK> getDomainRepository(){
        final JpaRepository<E, PK> repository = repositoryMap.get(supportedDomainClass());
        Assert.notNull(repository, String.format("Domain object %s is not supported!", supportedDomainClass().getName()));
        return repository;
    }

    @Override
    public E findOne(final PK id) {
        Assert.notNull(id, "Entity id is required");
        final E entity = getDomainRepository().findOne(id);
        if(entity == null){
            throw new ResourceNotFoundException();
        }
        getGenericRepository().detach(entity);
        entity.flatEntity();
        return entity;
    }

    @Override
    public Collection<E> findAll(){
        final Collection<E> entities = getDomainRepository().findAll();
        unproxyEntity(entities);
        return entities;
    }

    @Override
    @Transactional(readOnly=false)
    public void delete(final PK id) {
        Assert.notNull(id, "Entity id is required");
        findOne(id);
        getDomainRepository().delete(id);
        getDomainRepository().flush();
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
        return getDomainRepository().saveAndFlush(entity);
    }

    protected void unproxyEntity(final E... entities){
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

    private GenericRepository<E> getGenericRepository() {
        return genericRepository;
    }

    @Autowired
    public void setGenericRepository(GenericRepository<E> genericRepository) {
        this.genericRepository = genericRepository;
    }

    public void setRepositoryMap(Map<Class<? extends BaseEntity>, JpaRepository<E, PK>> repositoryMap) {
        this.repositoryMap = repositoryMap;
    }
}
