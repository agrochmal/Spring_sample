package pl.demo.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.demo.core.model.entity.BaseEntity;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Robert on 12.01.15.
 */

public interface CRUDService<PK extends Serializable, E extends BaseEntity> {

    Collection<E> findAll();

    default Page<E> findAll(Pageable pageable){
        return null;
    }

    E findOne(PK id);

    void delete(PK id);

    void edit(PK id, E entity);

    E save(E entity);
}
