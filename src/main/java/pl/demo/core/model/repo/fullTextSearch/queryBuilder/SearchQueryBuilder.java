package pl.demo.core.model.repo.fullTextSearch.queryBuilder;

import org.apache.lucene.search.Query;

import java.util.Optional;

/**
 * Created by robertsikora on 29.09.15.
 */
public interface SearchQueryBuilder {

    Optional<Query> build(org.hibernate.search.query.dsl.QueryBuilder queryBuilder);
}
