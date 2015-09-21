package pl.ppl.common.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.ppl.common.infrastructure.repository.FetchableJpaRepository;
import pl.ppl.common.infrastructure.repository.FetchableJpaRepositoryFactoryBean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables Spring Data support for {@link FetchableJpaRepository}
 *
 * This annotation is intended to be put on configuration classes.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableJpaRepositories(repositoryFactoryBeanClass = FetchableJpaRepositoryFactoryBean.class)
public @interface EnableCustomSpringDataJpaRepositories {

    /**
     * Base packages to scan for annotated components.
     */
    Class<?>[] basePackageClasses() default {};
}
