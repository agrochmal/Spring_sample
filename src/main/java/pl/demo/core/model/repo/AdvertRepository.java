package pl.demo.core.model.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.repo.fullTextSearch.SearchableRepository;

import java.util.Collection;

public interface AdvertRepository extends JpaRepository<Advert, Long>, SearchableRepository<Advert> {

	Collection<Advert> findByUserId(@Param("id") Long userId);

	Page<Advert> findByActive(Boolean active, Pageable pageable);

	Long countByActive(Boolean active);
}
