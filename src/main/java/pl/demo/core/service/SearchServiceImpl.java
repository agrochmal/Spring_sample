package pl.demo.core.service;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
public class SearchServiceImpl implements SearchService{

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public Page<Advert> searchAdverts(final SearchCriteriaDTO searchCriteriaDTO, final Pageable pageable) {
        final EntityManager em = entityManagerFactory.createEntityManager();;
        try {
            final FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
            em.getTransaction().begin();

            final QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Advert.class).get();

            org.apache.lucene.search.Query locationQuery = null;
            org.apache.lucene.search.Query keywordQuery = null;

            if (searchCriteriaDTO.hasLocation()) {
                final double radius = searchCriteriaDTO.getLocRadius();
                locationQuery = qb.spatial()
                        .onDefaultCoordinates()
                        .within(radius, Unit.KM)
                        .ofLatitude(searchCriteriaDTO.getLocLatitude())
                        .andLongitude(searchCriteriaDTO.getLocLongitude())
                        .createQuery();
            }

            if (searchCriteriaDTO.hasKeyword()) {
                keywordQuery = qb
                        .keyword()
                        .onFields("title", "description")
                        .matching(searchCriteriaDTO.getKeyWords())
                        .createQuery();
            }

            final BooleanQuery bq = new BooleanQuery();
            if (null != locationQuery) {
                bq.add(new BooleanClause(locationQuery, BooleanClause.Occur.MUST));
            }
            if (null != keywordQuery) {
                bq.add(new BooleanClause(keywordQuery, BooleanClause.Occur.MUST));
            }
            final javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(bq, Advert.class);
            final int total = jpaQuery.getResultList().size();
            final int pageSize = pageable.getPageSize();
            final int pageStart = pageable.getPageNumber() * pageSize;
            jpaQuery.setFirstResult(pageStart);
            jpaQuery.setMaxResults(pageSize);

            final List result = jpaQuery.getResultList();
            em.getTransaction().commit();

            return new Page<Advert>() {
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
                public List<Advert> getContent() {
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
                public Iterator<Advert> iterator() {
                    return null;
                }
            };

        }finally {
            em.close();
        }
    }

    private static final class CreateriaBuilder{
        //to-do
    }

    private static final class PageBuider{
        //to-do
    }
}
