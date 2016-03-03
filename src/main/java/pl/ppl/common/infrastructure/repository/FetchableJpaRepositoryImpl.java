package pl.ppl.common.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

public class FetchableJpaRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements FetchableJpaRepository<T, ID> {

    private EntityManager em;

    public FetchableJpaRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.em = entityManager;
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

    @Override
    public List<T> findAll(Specification<T> spec, EntityGraph.EntityGraphType entityGraphType, String entityGraphName) {
        TypedQuery<T> query = getQuery(spec, (Sort) null);
        query.setHint(entityGraphType.getKey(), em.getEntityGraph(entityGraphName));
        return query.getResultList();
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable, EntityGraph.EntityGraphType entityGraphType, String entityGraphName) {
        TypedQuery<T> query = getQuery(spec, pageable.getSort());
        query.setHint(entityGraphType.getKey(), em.getEntityGraph(entityGraphName));
        return readPage(query, pageable, spec);
    }

    @Override
    public List<T> findAll(Specification<T> spec, Sort sort, EntityGraph.EntityGraphType entityGraphType, String entityGraphName) {
        TypedQuery<T> query = getQuery(spec, sort);
        query.setHint(entityGraphType.getKey(), em.getEntityGraph(entityGraphName));
        return query.getResultList();
    }

    @Override
    public T findOne(Specification<T> spec, EntityGraph.EntityGraphType entityGraphType, String entityGraphName) {
        TypedQuery<T> query = getQuery(spec, (Sort) null);
        query.setHint(entityGraphType.getKey(), em.getEntityGraph(entityGraphName));
        return query.getSingleResult();
    }
}
