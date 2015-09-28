package pl.demo.core.model.repo.fullTextSearch;

import org.apache.lucene.search.BooleanQuery;
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
import pl.demo.core.model.entity.Advert;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by robertsikora on 28.09.15.
 */

public class AdvertSearchRepositoryImpl extends SimpleJpaRepository<Advert, Long>
        implements SearchableRepository<Advert>{

    public AdvertSearchRepositoryImpl(final Class<Advert> domainClass, final EntityManager em) {
        super(domainClass, em);
        initialize(em);
    }

    private EntityManagerProxy entityManagerProxy;

    @Override
    public Page<Advert> search(final SearchCriteria searchCriteria, final Pageable pageable) {
        Assert.notNull(searchCriteria, "searchCriteria is required");
        Assert.notNull(pageable, "pageable is required");

        final EntityManager em = getFullTextEntityManager();
        final FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
        final QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Advert.class).get();
        final BooleanQuery bq = new CriteriaBuilder(searchCriteria, qb)
                .applyLocationCriteria()
                .applyKeywordCriteria()
                .build();
        final FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(bq, Advert.class);
        final int total = fullTextQuery.getResultSize();
        fullTextQuery.setFirstResult(pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        final List result = fullTextQuery.getResultList();
        return new PageImpl(result, pageable, total);
    }

    public void initialize(final EntityManager entityManager) {
        if(!(entityManager instanceof EntityManagerProxy)) {
            throw new FatalBeanException("Entity manager " + entityManager + " was not a proxy");
        }
        this.entityManagerProxy = (EntityManagerProxy)entityManager;
    }

    private FullTextEntityManager getFullTextEntityManager() {
        return Search.getFullTextEntityManager(this.entityManagerProxy.getTargetEntityManager());
    }
}
