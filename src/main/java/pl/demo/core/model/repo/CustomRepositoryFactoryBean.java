package pl.demo.core.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.repo.fullTextSearch.AdvertSearchRepositoryImpl;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by robertsikora on 28.09.15.
 */
public class CustomRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable> extends
        JpaRepositoryFactoryBean<R, T, I> {

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(final EntityManager entityManager) {
        return new CustomRepositoryFactory(entityManager);
    }

    private static class CustomRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {

        private EntityManager entityManager;

        public CustomRepositoryFactory(final EntityManager entityManager) {
            super(entityManager);
            this.entityManager = entityManager;
        }

        @Override
        protected Object getTargetRepository(final RepositoryMetadata metadata) {
            if(metadata.getDomainType().getName().equals(Advert.class.getName())){
                return new AdvertSearchRepositoryImpl((Class<Advert>)metadata.getDomainType(), entityManager);
            }
            return super.getTargetRepository(metadata);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(final RepositoryMetadata metadata) {
            if(metadata.getDomainType().getName().equals(Advert.class.getName())){
                return AdvertSearchRepositoryImpl.class;
            }
            return super.getRepositoryBaseClass(metadata);
        }
    }
}
