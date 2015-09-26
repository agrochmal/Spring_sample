package pl.demo.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import pl.demo.core.model.entity.Advert;
import pl.demo.web.dto.EMailDTO;
import pl.demo.web.dto.SearchCriteriaDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Validated
public interface AdvertService extends CRUDService<Long, Advert> {
	@NotNull @Valid
	Page<Advert> findAll(@NotNull Pageable pageable);

	@NotNull @Valid
	Collection<Advert> findByUserId(@NotNull @Min(1) Long id);

	@NotNull
	Page<Advert> findBySearchCriteria(@NotNull SearchCriteriaDTO searchCriteriaDTO, @NotNull Pageable pageable);

	@NotNull
	Advert createNew();

	void sendMail(@Valid EMailDTO eMailDTO);

	void updateActiveStatus(@NotNull @Min(1) Long id, @NotNull Boolean status);
}
