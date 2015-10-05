package pl.demo.core.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.demo.core.model.entity.MediaResource;

import javax.persistence.LockModeType;

public interface MediaResourceRepository extends JpaRepository<MediaResource, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM MediaResource m WHERE m.id=:id")
    MediaResource findOneForUpdate(@Param("id") Long id);

    @Query("SELECT m FROM MediaResource m WHERE m.id = (select MIN(m2.id) from MediaResource m2 where m2.advert.id=:id)")
    MediaResource findFirstByAdvertOrderByEntryDateDesc(@Param("id") Long id);
}

