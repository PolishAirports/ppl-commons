package pl.ppl.common.infrastructure.repository;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.io.Serializable;

public class FetchableJpaRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements FetchableJpaRepository<T, ID> {

    public FetchableJpaRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
    }

    @Override
    public T fetchById(ID id) {
        T entity = findOne(id);
        if (entity == null) {
            String msg = getDomainClass().getSimpleName() + " entity with id=" + id + " does not exist";
            throw new EntityNotFoundException(msg);
        }
        return entity;
    }
}
