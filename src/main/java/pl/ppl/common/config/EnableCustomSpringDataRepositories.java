package pl.ppl.common.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.ppl.common.infrastructure.repository.FetchableJpaRepositoryFactoryBean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableJpaRepositories(repositoryFactoryBeanClass = FetchableJpaRepositoryFactoryBean.class)
public @interface EnableCustomSpringDataRepositories {
}
