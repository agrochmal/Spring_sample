package pl.demo.core.service.advert;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.service.basic_service.CRUDService;
import pl.demo.web.dto.EMailDTO;
import pl.demo.web.dto.SearchCriteriaDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Validated
public interface AdvertService extends CRUDService<Long, Advert> {

	@NotNull
	@Valid
	Page<Advert> findAll(@NotNull Pageable pageable);

	@PreAuthorize("isAuthenticated()")
	@NotNull
	@Valid
	Collection<Advert> findByUserId(@NotNull @Min(1) long id);

	@NotNull
	Page<Advert> findBySearchCriteria(@NotNull SearchCriteriaDTO searchCriteriaDTO, @NotNull Pageable pageable);

	@PreAuthorize("isAuthenticated()")
	@NotNull
	Advert createNew();

	@PreAuthorize("isAuthenticated()")
	void sendMail(@NotNull @Valid EMailDTO eMailDTO);

	@PreAuthorize("isAuthenticated()")
	void updateActiveStatus(@NotNull @Min(1) long id, @NotNull Boolean status);
}
