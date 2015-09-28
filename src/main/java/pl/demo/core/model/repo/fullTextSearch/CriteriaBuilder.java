package pl.demo.core.model.repo.fullTextSearch;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import pl.demo.web.dto.SearchCriteriaDTO;

/**
 * Created by robertsikora on 28.09.15.
 */
public class CriteriaBuilder {

    private final static String TITLE_FIELD = "title";
    private final static String TITLE_DESCRIPTION = "description";

    private final SearchCriteria searchCriteria;
    private final QueryBuilder qb;
    private Query locationQuery = null;
    private Query keywordQuery = null;

    public CriteriaBuilder(final SearchCriteria searchCriteria, final QueryBuilder qb) {
        this.searchCriteria = searchCriteria;
        this.qb = qb;
    }

    public CriteriaBuilder applyKeywordCriteria(){
        final SearchCriteriaDTO searchCriteria = (SearchCriteriaDTO)this.searchCriteria;
        if (searchCriteria.hasKeyword()) {
            keywordQuery = qb
                    .keyword()
                    .onFields(TITLE_FIELD, TITLE_DESCRIPTION)
                    .matching(searchCriteria.getKeyWords())
                    .createQuery();
        }
        return this;
    }

    public CriteriaBuilder applyLocationCriteria(){
        final SearchCriteriaDTO searchCriteria = (SearchCriteriaDTO)this.searchCriteria;
        if (searchCriteria.hasLocation()) {
            final double radius = searchCriteria.getLocRadius();
            locationQuery = qb.spatial()
                    .onDefaultCoordinates()
                    .within(radius, Unit.KM)
                    .ofLatitude(searchCriteria.getLocLatitude())
                    .andLongitude(searchCriteria.getLocLongitude())
                    .createQuery();
        }
        return this;
    }

    public BooleanQuery build(){
        final BooleanQuery bq = new BooleanQuery();
        if (null != locationQuery) {
            bq.add(new BooleanClause(locationQuery, BooleanClause.Occur.MUST));
        }
        if (null != keywordQuery) {
            bq.add(new BooleanClause(keywordQuery, BooleanClause.Occur.MUST));
        }
        return bq;
    }
}
