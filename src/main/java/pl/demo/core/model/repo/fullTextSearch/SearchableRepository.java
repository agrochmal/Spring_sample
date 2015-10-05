package pl.demo.core.model.repo.fullTextSearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.demo.core.model.repo.fullTextSearch.queryBuilder.SearchQueryBuilder;

/**
 * Created by robertsikora on 28.09.15.
 */
public interface SearchableRepository<T> {

    Page<T> search(SearchQueryBuilder searchQueryBuilder, Pageable pageable);
}
