package pl.ppl.common.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pl.ppl.common.config.EnableCustomSpringDataJpaRepositories;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;

/**
 * Enriches Spring Data {@link JpaRepository} with ability to fetch an entity by id and fail if such entity does not exists in the database.
 *
 * To use this repository you have to make Spring Data infrastructure aware of {@code FetchableJpaRepositoryImpl}.
 * You can do that by putting {@link EnableCustomSpringDataJpaRepositories} annotation on your configuration class.
 */
@NoRepositoryBean
public interface FetchableJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * Retrieves an entity by its id or throws {@code EntityNotFoundException} if such entity does not exists.
     *
     * Please bare in mind that Spring Framework automatically translates {@code EntityNotFoundException} into {@code JpaObjectRetrievalFailureException}.
     *
     * @param id must not be {@code null}.
     * @return the entity with the given id
     * @throws EntityNotFoundException if entity with the given id does not exists
     */
    T fetchById(ID id);
}
