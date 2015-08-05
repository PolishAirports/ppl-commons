package pl.ppl.common.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;

@NoRepositoryBean
public interface FetchableJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * Retrieves an entity by its id or thorws EntityNotFoundException if such entity does not exists.
     *
     * Please bare in mind that Spring Framework automatically translates EntityNotFoundException into JpaObjectRetrievalFailureException.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id
     * @throws EntityNotFoundException if entity with the given id does not exists
     */
    T fetchById(ID id);
}
