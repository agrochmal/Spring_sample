package pl.demo.core.model.repo.fullTextSearch.queryBuilder;

import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import pl.demo.web.dto.SearchCriteriaDTO;

import java.util.Optional;

/**
 * Created by robertsikora on 29.09.15.
 */
public class CommentSearchQueryBuiderImpl implements SearchQueryBuilder{

    private final static String TEXT_FIELD = "text";

    private final SearchCriteriaDTO searchCriteria;

    public CommentSearchQueryBuiderImpl(final SearchCriteriaDTO searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    private Query applyKeywordCriteria(final QueryBuilder qb){
        if (searchCriteria.hasKeyword()) {
            return qb
                    .keyword()
                    .onFields(TEXT_FIELD)
                    .matching(searchCriteria.getKeyWords())
                    .createQuery();
        }
        return null;
    }

    @Override
    public Optional<Query> build(QueryBuilder queryBuilder) {
        return Optional.of(applyKeywordCriteria(queryBuilder));
    }
}
