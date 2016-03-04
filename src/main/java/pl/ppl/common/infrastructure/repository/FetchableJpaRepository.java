package pl.ppl.common.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pl.ppl.common.config.EnableCustomSpringDataJpaRepositories;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.List;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

/**
 * Enriches Spring Data {@link JpaRepository} with:
 * - ability to fetch an entity by id and fail if such entity does not exists in the database.
 * - ability to search entities using JPA Specification, Entity Graphs with sorting and paging
 *
 * To use this repository you have to make Spring Data infrastructure aware of {@code FetchableJpaRepositoryImpl}.
 * You can do that by putting {@link EnableCustomSpringDataJpaRepositories} annotation on your configuration class.
 */
@NoRepositoryBean
public interface FetchableJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * Retrieves an entity by its id or throws {@code EntityNotFoundException} if such entity does not exists.
     *
     * Please bare in mind that Spring Framework automatically translates {@code EntityNotFoundException}
     * into {@code JpaObjectRetrievalFailureException}.
     *
     * @param id must not be {@code null}.
     * @return the entity with the given id
     * @throws EntityNotFoundException if entity with the given id does not exists
     */
    T fetchById(ID id);

    /**
     * Retrieve all entities matching the given {@link Specification} and retrieve them using provided
     * {@link javax.persistence.EntityGraph}.
     *
     * @param spec
     * @param entityGraphType
     * @param entityGraphName
     * @return
     */
    List<T> findAll(Specification<T> spec, EntityGraphType entityGraphType, String entityGraphName);

    /**
     * Retrieve all entities matching the given {@link Specification}, paged according to {@link Pageable}
     * and retrieve them using provided {@link javax.persistence.EntityGraph}.
     *
     * @param spec
     * @param pageable
     * @param entityGraphType
     * @param entityGraphName
     * @return
     */
    Page<T> findAll(Specification<T> spec, Pageable pageable, EntityGraphType entityGraphType, String entityGraphName);

    /**
     * Retrieve all entities matching the given {@link Specification}, sorted according to {@link Sort}
     * and retrieve them using provided {@link javax.persistence.EntityGraph}.
     *
     * @param spec
     * @param sort
     * @param entityGraphType
     * @param entityGraphName
     * @return
     */
    List<T> findAll(Specification<T> spec, Sort sort, EntityGraphType entityGraphType, String entityGraphName);

    /**
     * Retrieve one entity matching the given {@link Specification}, sorted according to {@link Sort}
     * and retrieve them using provided {@link javax.persistence.EntityGraph}.
     *
     * @param spec
     * @param entityGraphType
     * @param entityGraphName
     * @return
     */
    T findOne(Specification<T> spec, EntityGraphType entityGraphType, String entityGraphName);
}
