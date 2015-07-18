package pl.demo.core.model.repo;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.demo.core.model.entity.Advert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public interface AdvertRepository extends JpaRepository<Advert, Long> {

	@Query("SELECT a FROM Advert a WHERE a.user.id = :id")
	Collection<Advert> findByUserId(@Param("id") Long userId);

	Collection<Advert> findByActive(Boolean active);
	Page<Advert> findByActive(Boolean active, Pageable pageable);
}
