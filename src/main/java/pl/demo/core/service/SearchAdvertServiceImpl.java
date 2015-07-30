package pl.demo.core.service;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.demo.core.model.entity.Advert;
import pl.demo.web.dto.SearchCriteriaDTO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Robert on 2014-12-03.
 */
@Service
public class SearchAdvertServiceImpl implements SearchAdvertService {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public Page<Advert> searchAdverts(final SearchCriteriaDTO searchCriteriaDTO, final Pageable pageable) {
        Assert.notNull(searchCriteriaDTO, "searchCriteriaDTO is required");
        Assert.notNull(pageable, "pageable is required");

        final EntityManager em = entityManagerFactory.createEntityManager();
        try {
            final FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
            em.getTransaction().begin();

            final QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Advert.class).get();
            final BooleanQuery bq = new CreateriaBuilder(searchCriteriaDTO, qb)
                    .applyLocationCreateria()
                    .applyKeywordCreateria()
                    .build();
            final javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(bq, Advert.class);
            final int total = jpaQuery.getResultList().size();
            final int pageSize = pageable.getPageSize();
            final int pageStart = pageable.getPageNumber() * pageSize;
            jpaQuery.setFirstResult(pageStart);
            jpaQuery.setMaxResults(pageSize);

            final List result = jpaQuery.getResultList();
            em.getTransaction().commit();

            return new PageBuilder<Advert>(result, total, pageable).build();

        }finally {
            em.close();
        }
    }

    private static final class CreateriaBuilder{

        private final SearchCriteriaDTO searchCriteriaDTO;
        private final QueryBuilder qb;
        private Query locationQuery = null;
        private Query keywordQuery = null;

        public CreateriaBuilder(final SearchCriteriaDTO searchCriteriaDTO, final QueryBuilder qb) {
            this.searchCriteriaDTO = searchCriteriaDTO;
            this.qb = qb;
        }

        public CreateriaBuilder applyKeywordCreateria(){
            if (searchCriteriaDTO.hasKeyword()) {
                keywordQuery = qb
                        .keyword()
                        .onFields("title", "description")
                        .matching(searchCriteriaDTO.getKeyWords())
                        .createQuery();
            }
            return this;
        }

        public CreateriaBuilder applyLocationCreateria(){
            if (searchCriteriaDTO.hasLocation()) {
                final double radius = searchCriteriaDTO.getLocRadius();
                locationQuery = qb.spatial()
                        .onDefaultCoordinates()
                        .within(radius, Unit.KM)
                        .ofLatitude(searchCriteriaDTO.getLocLatitude())
                        .andLongitude(searchCriteriaDTO.getLocLongitude())
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

    private static final class PageBuilder<T>{

        private final List result;
        private final long total;
        private final Pageable pageable;


        private PageBuilder(final List result, final long total, final Pageable pageable) {
            this.result = result;
            this.total = total;
            this.pageable = pageable;
        }

        public Page<T> build(){

            validate();

            return new Page<T>() {
                @Override
                public int getTotalPages() {
                    return  new Long( getTotalElements() / getSize() ).intValue() + 1;
                }

                @Override
                public long getTotalElements() {
                    return total;
                }

                @Override
                public int getNumber() {
                    return pageable.getPageNumber();
                }

                @Override
                public int getSize() {
                    return pageable.getPageSize();
                }

                @Override
                public int getNumberOfElements() {
                    return result.size();
                }

                @Override
                public List<T> getContent() {
                    return result;
                }

                @Override
                public boolean hasContent() {
                    return !result.isEmpty();
                }

                @Override
                public Sort getSort() {
                    return null;
                }

                @Override
                public boolean isFirst() {
                    return getNumber() == 1;
                }

                @Override
                public boolean isLast() {
                    return getNumber() == getTotalPages();
                }

                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public boolean hasPrevious() {
                    return false;
                }

                @Override
                public Pageable nextPageable() {
                    return null;
                }

                @Override
                public Pageable previousPageable() {
                    return null;
                }

                @Override
                public Iterator<T> iterator() {
                    return null;
                }
            };
        }

        private void validate(){
            Assert.state(null!=result && result.size() != total, "Inconsistent data!");
        }
    }
}
