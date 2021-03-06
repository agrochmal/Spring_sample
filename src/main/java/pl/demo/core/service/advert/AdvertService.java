package pl.demo.core.service.advert;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.service.basicservice.CRUDService;
import pl.demo.web.dto.EMail;
import pl.demo.web.dto.SearchCriteria;

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
	Collection<Advert> findByUserId(@NotNull @Min(1) Long id);

	@NotNull
	Page<Advert> findBySearchCriteria(@NotNull SearchCriteria searchCriteriaDTO, @NotNull Pageable pageable);

	@PreAuthorize("isAuthenticated()")
	@NotNull
	Advert createNew();

	@PreAuthorize("isAuthenticated()")
	void sendMail(@NotNull @Valid EMail eMailDTO);

	@PreAuthorize("isAuthenticated()")
	void updateActiveStatus(@NotNull @Min(1) Long id, @NotNull Boolean status);
}
