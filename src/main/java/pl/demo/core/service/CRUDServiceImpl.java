package pl.demo.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.util.Assert;
import pl.demo.core.aspects.DetachEntity;
import pl.demo.core.model.entity.BaseEntity;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * Created by robertsikora on 27.07.15.
 */


public abstract class CRUDServiceImpl<PK extends Serializable, E extends BaseEntity> implements CRUDService<PK, E>{

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private Map<Class<? extends BaseEntity>, JpaRepository<E, PK>> repositoryMap;
    private Class<E> entityClass;

    public CRUDServiceImpl() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        while(!(genericSuperclass instanceof ParameterizedType)) {
            if(!(genericSuperclass instanceof Class)) {
                throw new IllegalStateException("Unable to determine type arguments because generic superclass neither parameterized type nor class.");
            }
            if (genericSuperclass == CRUDServiceImpl.class) {
                throw new IllegalStateException("Unable to determine type arguments because no parameterized generic superclass found.");
            }
            genericSuperclass = ((Class)genericSuperclass).getGenericSuperclass();
        }
        final ParameterizedType type = (ParameterizedType)genericSuperclass;
        final Type[] arguments = type.getActualTypeArguments();
        this.entityClass = (Class<E>)arguments[1];
    }

    @Transactional(readOnly = true)
    @DetachEntity
    @Override
    public E findOne(final PK id) {
        Assert.notNull(id, "Entity id is required");
        final E entity = getDomainRepository().findOne(id);
        Assert.notResourceFound(entity);
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

    protected JpaRepository<E, PK> getDomainRepository(){
        final JpaRepository<E, PK> repository = repositoryMap.get(entityClass);
        Assert.notNull(repository, String.format("Domain object %s is not supported!", entityClass.getSimpleName()));
        return repository;
    }

    public void setRepositoryMap(Map<Class<? extends BaseEntity>, JpaRepository<E, PK>> repositoryMap) {
        this.repositoryMap = repositoryMap;
    }
}
