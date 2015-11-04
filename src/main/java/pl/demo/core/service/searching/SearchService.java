package pl.demo.core.service.searching;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.Comment;
import pl.demo.web.dto.SearchCriteriaDTO;

import javax.validation.constraints.NotNull;

/**
 * Created by robertsikora on 27.07.15.
 */

@Validated
public interface SearchService {

    @NotNull
    Page<Advert> searchAdverts(@NotNull SearchCriteriaDTO searchCriteriaDTO, @NotNull Pageable pageable);

    @NotNull
    Page<Comment> searchComments(@NotNull SearchCriteriaDTO searchCriteriaDTO, @NotNull Pageable pageable);
}
