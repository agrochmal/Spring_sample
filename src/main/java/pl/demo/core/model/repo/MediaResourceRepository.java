package pl.demo.core.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.demo.core.model.entity.MediaResource;

import javax.persistence.LockModeType;

public interface MediaResourceRepository extends JpaRepository<MediaResource, Long>{

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from MediaResource m where m.id=:id")
    MediaResource findOneForUpdate(@Param("id") Long id);
}

