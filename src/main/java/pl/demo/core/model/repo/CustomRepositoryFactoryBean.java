package pl.demo.core.model.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import pl.demo.core.model.repo.fullTextSearch.SearchableRepository;
import pl.demo.core.model.repo.fullTextSearch.SearchableRepositoryImpl;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
            if(isSearchable(metadata)){
                return new SearchableRepositoryImpl< T, I>((Class<T>)metadata.getDomainType(), entityManager);
            }
            return super.getTargetRepository(metadata);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(final RepositoryMetadata metadata) {
            if(isSearchable(metadata)){
                return SearchableRepositoryImpl.class;
            }
            return super.getRepositoryBaseClass(metadata);
        }

        private boolean isSearchable(final  RepositoryMetadata repositoryMetadata){
            for(final Type type :repositoryMetadata.getRepositoryInterface().getGenericInterfaces()) {
                final ParameterizedType parameterizedType = (ParameterizedType) type;
                if (parameterizedType.getRawType().getTypeName().equals(SearchableRepository.class.getName())) {
                    return true;
                }
            }
            return false;
        }
    }
}
