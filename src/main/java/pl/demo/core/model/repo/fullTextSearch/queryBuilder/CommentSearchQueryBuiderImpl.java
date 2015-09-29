package pl.demo.core.model.repo.fullTextSearch.queryBuilder;

import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import pl.demo.web.dto.SearchCriteriaDTO;

/**
 * Created by robertsikora on 29.09.15.
 */
public class CommentSearchQueryBuiderImpl implements SearchQueryBuilder{

    private final SearchCriteriaDTO searchCriteria;

    public CommentSearchQueryBuiderImpl(final SearchCriteriaDTO searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Query build(QueryBuilder queryBuilder) {
        return null;
    }
}
