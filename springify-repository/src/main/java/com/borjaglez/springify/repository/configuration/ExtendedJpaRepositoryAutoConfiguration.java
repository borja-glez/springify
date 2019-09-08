package com.borjaglez.springify.repository.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.borjaglez.springify.repository.annotation.EnableExtendedJpaRepositories;

@Configuration
@ConditionalOnClass(EnableExtendedJpaRepositories.class)
@Import(ExtendedJpaRepositoryConfig.class)
public class ExtendedJpaRepositoryAutoConfiguration {

}
