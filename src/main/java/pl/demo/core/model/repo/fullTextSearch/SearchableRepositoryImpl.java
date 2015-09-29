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
import org.springframework.util.Assert;
import pl.demo.core.model.repo.fullTextSearch.queryBuilder.SearchQueryBuilder;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * Created by robertsikora on 28.09.15.
 */

public class SearchableRepositoryImpl<T, I extends Serializable> extends SimpleJpaRepository<T, I>
        implements SearchableRepository<T>{

    private EntityManagerProxy entityManagerProxy;
    private Class<T> entityClass;

    public SearchableRepositoryImpl(final Class<T> domainClass, final EntityManager em) {
        super(domainClass, em);
        this.entityClass = domainClass;
        initEntityManager(em);
    }

    private void initEntityManager(final EntityManager entityManager) {
        if(!(entityManager instanceof EntityManagerProxy)) {
            throw new FatalBeanException("Entity manager " + entityManager + " was not a proxy");
        }
        this.entityManagerProxy = (EntityManagerProxy)entityManager;
    }

    @Override
    public Page<T> search(final SearchQueryBuilder searchQueryBuilder, final Pageable pageable) {
        Assert.notNull(searchQueryBuilder, "searchQueryBuilder is required");
        Assert.notNull(pageable, "pageable is required");

        final EntityManager em = getFullTextEntityManager();
        final FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
        final QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(entityClass).get();
        final Query query = searchQueryBuilder.build(qb);
        final FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, entityClass);
        final int total = fullTextQuery.getResultSize();
        fullTextQuery.setFirstResult(pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        final List result = fullTextQuery.getResultList();
        return new PageImpl(result, pageable, total);
    }

    private FullTextEntityManager getFullTextEntityManager() {
        return Search.getFullTextEntityManager(this.entityManagerProxy.getTargetEntityManager());
    }
}
