package pl.demo.core.model.repo.fullTextSearch.queryBuilder;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import pl.demo.web.dto.SearchCriteriaDTO;

/**
 * Created by robertsikora on 28.09.15.
 */
public class AdvertSearchQueryBuilderImpl implements SearchQueryBuilder {

    private final static String TITLE_FIELD = "title";
    private final static String TITLE_DESCRIPTION = "description";

    private final SearchCriteriaDTO searchCriteria;

    public AdvertSearchQueryBuilderImpl(final SearchCriteriaDTO searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    private Query applyKeywordCriteria(final QueryBuilder qb){
        if (searchCriteria.hasKeyword()) {
            return qb
                .keyword()
                .onFields(TITLE_FIELD, TITLE_DESCRIPTION)
                .matching(searchCriteria.getKeyWords())
                .createQuery();
        }
        return null;
    }

    private Query applyLocationCriteria(final QueryBuilder qb){
        if (searchCriteria.hasLocation()) {
            final double radius = searchCriteria.getLocRadius();
            return qb
                .spatial()
                .onDefaultCoordinates()
                .within(radius, Unit.KM)
                .ofLatitude(searchCriteria.getLocLatitude())
                .andLongitude(searchCriteria.getLocLongitude())
                .createQuery();
        }
        return null;
    }

    @Override
    public Query build(final QueryBuilder queryBuilder) {
        final Query locationQuery = applyLocationCriteria(queryBuilder);
        final BooleanQuery bq = new BooleanQuery();
        if (null != locationQuery) {
            bq.add(new BooleanClause(locationQuery, BooleanClause.Occur.MUST));
        }
        final Query keywordQuery = applyKeywordCriteria(queryBuilder);
        if (null != keywordQuery) {
            bq.add(new BooleanClause(keywordQuery, BooleanClause.Occur.MUST));
        }
        return bq;
    }
}
