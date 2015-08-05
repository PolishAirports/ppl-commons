package pl.ppl.common.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class FetchableJpaRepositoryFactoryBean<R extends JpaRepository<T, ID>, T, ID extends Serializable>
        extends JpaRepositoryFactoryBean<R, T, ID> {

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
        return new FetchableJpaRepositoryFactory(em);
    }

    private static class FetchableJpaRepositoryFactory<T, ID extends Serializable>
            extends JpaRepositoryFactory {

        private final EntityManager em;

        public FetchableJpaRepositoryFactory(EntityManager em) {
            super(em);
            this.em = em;
        }

        @Override
        protected Object getTargetRepository(RepositoryMetadata metadata) {
            return new FetchableJpaRepositoryImpl<T, ID>((Class<T>) metadata.getDomainType(), em);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return FetchableJpaRepositoryImpl.class;
        }
    }
}
