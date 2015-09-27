package pl.demo.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.demo.core.aspects.DetachEntity;
import pl.demo.core.model.entity.BaseEntity;
import pl.demo.web.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * Created by robertsikora on 27.07.15.
 */


public abstract class CRUDServiceImpl<PK extends Serializable, E extends BaseEntity> implements CRUDService<PK, E>{

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private Map<Class<? extends BaseEntity>, JpaRepository<E, PK>> repositoryMap;

    protected abstract Class<E> supportedDomainClass();

    protected JpaRepository<E, PK> getDomainRepository(){
        final JpaRepository<E, PK> repository = repositoryMap.get(supportedDomainClass());
        Assert.notNull(repository, String.format("Domain object %s is not supported!", supportedDomainClass().getName()));
        return repository;
    }

    @Transactional(readOnly = true)
    @DetachEntity
    @Override
    public E findOne(final PK id) {
        Assert.notNull(id, "Entity id is required");
        final E entity = getDomainRepository().findOne(id);
        if(entity == null){
            throw new ResourceNotFoundException();
        }
        return entity;
    }

    @Transactional(readOnly = true)
    @DetachEntity
    @Override
    public Collection<E> findAll(){
        return getDomainRepository().findAll();
    }

    @Transactional
    @Override
    public void delete(final PK id) {
        Assert.notNull(id, "Entity id is required");
        findOne(id);
        getDomainRepository().delete(id);
        getDomainRepository().flush();
    }

    @Transactional
    @Override
    public void edit(final PK id, final E entity) {
        throw new IllegalArgumentException("Method not supported yet!");
    }

    @Transactional
    @Override
    public E save(final E entity) {
        Assert.notNull(entity, "Entity is required");
        return getDomainRepository().saveAndFlush(entity);
    }

    public void setRepositoryMap(Map<Class<? extends BaseEntity>, JpaRepository<E, PK>> repositoryMap) {
        this.repositoryMap = repositoryMap;
    }
}
