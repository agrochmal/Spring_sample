package pl.demo.core.model.repo.fullTextSearch;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.FatalBeanException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.orm.jpa.EntityManagerProxy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.repo.fullTextSearch.queryBuilder.SearchQueryBuilder;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;

/**
 * Created by robertsikora on 28.09.15.
 */

public class SearchableRepositoryImpl<T, I extends Serializable> extends SimpleJpaRepository<T, I>
        implements SearchableRepository<T> {

    private EntityManagerProxy entityManagerProxy;
    private Class<T> domainClass;

    public SearchableRepositoryImpl(final Class<T> domainClass, final EntityManager em) {
        super(domainClass, em);
        this.domainClass = domainClass;
        initEntityManager(em);
    }

    private void initEntityManager(final EntityManager entityManager) {
        if(!(entityManager instanceof EntityManagerProxy)) {
            throw new FatalBeanException("Entity manager " + entityManager + " was not a proxy");
        }
        this.entityManagerProxy = (EntityManagerProxy)entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<T> search(final SearchQueryBuilder searchQueryBuilder, final Pageable pageable) {
        Assert.notNull(searchQueryBuilder, "searchQueryBuilder is required");
        Assert.notNull(pageable, "pageable is required");

        final EntityManager em = getFullTextEntityManager();
        final FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
        final QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(domainClass).get();
        final Optional<Query> queryOptional = searchQueryBuilder.build(qb);
        if(!queryOptional.isPresent()){
            return (Page<T>) EMPTY_PAGE;
        }

        final FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(queryOptional.get(), domainClass);
        fullTextQuery.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());
        return new PageImpl(fullTextQuery.getResultList(), pageable, fullTextQuery.getResultSize());
    }

    private FullTextEntityManager getFullTextEntityManager() {
        return Search.getFullTextEntityManager(this.entityManagerProxy.getTargetEntityManager());
    }
}
