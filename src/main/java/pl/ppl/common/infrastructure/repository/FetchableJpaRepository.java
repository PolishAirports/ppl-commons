package pl.ppl.common.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface FetchableJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    T fetchById(ID id);
}
