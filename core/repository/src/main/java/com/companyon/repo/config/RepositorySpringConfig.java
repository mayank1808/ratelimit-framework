package com.companyon.repo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.companyon.jpa.spring.config.JPAUtils;

/**
 * 
 */

/**
 * @author mayank
 *
 */

@Configuration
@EnableJpaRepositories(basePackages = "com.companyon.repository.repo")
@Import(JPAUtils.class)
public class RepositorySpringConfig {

}