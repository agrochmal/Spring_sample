package pl.demo.core.service.searching;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.Comment;
import pl.demo.web.dto.SearchCriteria;

import javax.validation.constraints.NotNull;

/**
 * Created by robertsikora on 27.07.15.
 */

@Validated
public interface SearchService {

    @PreAuthorize("isAuthenticated()")
    @NotNull
    Page<Advert> searchAdverts(@NotNull SearchCriteria searchCriteriaDTO, @NotNull Pageable pageable);

    @PreAuthorize("isAuthenticated()")
    @NotNull
    Page<Comment> searchComments(@NotNull SearchCriteria searchCriteriaDTO, @NotNull Pageable pageable);
}
