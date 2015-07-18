package pl.demo.core.service;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.demo.web.dto.SearchCriteriaDTO;
import pl.demo.core.model.entity.Advert;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Robert on 2014-12-03.
 */
@Service
public class SearchService {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public void rebuildIndexes() throws InterruptedException{

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();
    }

    public Page<Advert> select(SearchCriteriaDTO searchCriteriaDTO, Pageable pageable) {

        EntityManager em = entityManagerFactory.createEntityManager();
        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
        em.getTransaction().begin();

        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Advert.class).get();

        org.apache.lucene.search.Query locationQuery = null;
        org.apache.lucene.search.Query keywordQuery = null;

        if(searchCriteriaDTO.hasLocation()) {
            double radius = searchCriteriaDTO.getLocRadius();
            locationQuery = qb.spatial()
                    .onDefaultCoordinates()
                    .within(radius, Unit.KM)
                    .ofLatitude( searchCriteriaDTO.getLocLatitude() )
                    .andLongitude( searchCriteriaDTO.getLocLongitude() )
                    .createQuery();
        }

        if(searchCriteriaDTO.hasKeyword()){
            keywordQuery = qb
                    .keyword()
                    .onFields("title", "description")
                    .matching(searchCriteriaDTO.getKeyWords())
                    .createQuery();
        }

        BooleanQuery bq = new BooleanQuery();
        if(null!=locationQuery)
            bq.add(new BooleanClause(locationQuery, BooleanClause.Occur.MUST));
        if(null!=keywordQuery)
            bq.add(new BooleanClause(keywordQuery, BooleanClause.Occur.MUST));

        javax.persistence.Query jpaQuery  = fullTextEntityManager.createFullTextQuery(bq, Advert.class);

        int total = jpaQuery.getResultList().size();

        int pageSize = pageable.getPageSize();
        int pageStart = pageable.getPageNumber() * pageSize;
        jpaQuery.setFirstResult(pageStart);
        jpaQuery.setMaxResults(pageSize);

        List result = jpaQuery.getResultList();

        em.getTransaction().commit();
        em.close();

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
    }
}
