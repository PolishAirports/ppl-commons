package pl.ppl.common.infrastructure.repository

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Configuration
import pl.ppl.common.config.EnableCustomSpringDataJpaRepositories

@Configuration
@EnableAutoConfiguration
@EnableCustomSpringDataJpaRepositories
class RepositoriesConfig {
}
