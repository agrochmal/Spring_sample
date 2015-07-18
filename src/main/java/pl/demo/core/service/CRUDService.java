package pl.demo.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Collection;

/**
 * Created by Robert on 12.01.15.
 */
public interface CRUDService<PK, Entity> {

    default Collection<Entity> findAll(){
        return null;
    }

    default Page<Entity> findAll(Pageable pageable){
        return null;
    }

    Entity findOne(PK id);
    void delete(PK id);
    void edit(Entity entity);
    Entity save(Entity entity);

}
