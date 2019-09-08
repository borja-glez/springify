package com.borjaglez.springify.repository.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.borjaglez.springify.repository.jpa.ExtendedJpaRepositoryImpl;

@Configuration
@ComponentScan
@EnableJpaRepositories(repositoryBaseClass = ExtendedJpaRepositoryImpl.class)
public class ExtendedJpaRepositoryConfig {

}
