/**
 * 
 */
package com.companyon.repo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author mayank
 *
 */
@Configuration
@ComponentScan(basePackages = { "com.companyon.repository" })
@Import({ RepositorySpringConfig.class })
public class DaoSpringConfig {

}
