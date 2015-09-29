package pl.demo.core.model.repo.fullTextSearch.queryBuilder;

import org.apache.lucene.search.Query;

/**
 * Created by robertsikora on 29.09.15.
 */
public interface SearchQueryBuilder {
    Query build(org.hibernate.search.query.dsl.QueryBuilder queryBuilder);
}
