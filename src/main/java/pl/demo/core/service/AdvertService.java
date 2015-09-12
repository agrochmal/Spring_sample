package pl.demo.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import pl.demo.core.model.entity.Advert;
import pl.demo.web.dto.EMailDTO;
import pl.demo.web.dto.SearchCriteriaDTO;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Validated
public interface AdvertService extends CRUDService<Long, Advert> {
	@NotNull
	Page<Advert> findAll(@NotNull Pageable pageable);

	@NotNull
	Collection<Advert> findByUserId(@NotNull Long id);

	@NotNull
	Page<Advert> findBySearchCriteria(@NotNull SearchCriteriaDTO searchCriteriaDTO, @NotNull Pageable pageable);

	@NotNull
	Advert createNew();

	void sendMail(@NotNull EMailDTO eMailDTO);

	void updateActiveStatus(@NotNull Long id, @NotNull Boolean status);
}
