package pl.demo.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Robert on 12.01.15.
 */
public interface CRUDService<PK extends Serializable, E> {

    default Collection<E> findAll(){
        return null;
    }

    default Page<E> findAll(Pageable pageable){
        return null;
    }

    E findOne(PK id);

    void delete(PK id);

    void edit(E entity);

    E save(E entity);
}
