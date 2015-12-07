package pl.demo.core.model.repo.fullTextSearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.repo.fullTextSearch.queryBuilder.SearchQueryBuilder;

import java.util.Collections;

/**
 * Created by robertsikora on 28.09.15.
 */
public interface SearchableRepository<T> {

    Page<Advert> EMPTY_PAGE = new PageImpl<>(Collections.emptyList(), null, 0);

    Page<T> search(SearchQueryBuilder searchQueryBuilder, Pageable pageable);
}
