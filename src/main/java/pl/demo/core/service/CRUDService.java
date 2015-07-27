package pl.demo.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.model.entity.BaseEntity;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Robert on 12.01.15.
 */
public interface CRUDService<PK extends Serializable, E extends BaseEntity> {

    default Collection<E> findAll(){
        return null;
    }

    default Page<E> findAll(Pageable pageable){
        return null;
    }

    E findOne(PK id);

    @Transactional(readOnly=false)
    void delete(PK id);

    @Transactional(readOnly=false)
    void edit(E entity);

    @Transactional(readOnly=false)
    E save(E entity);
}
