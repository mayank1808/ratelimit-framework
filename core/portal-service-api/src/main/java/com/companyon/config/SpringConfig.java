/**
 * 
 */
package com.companyon.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.companyon.repo.config.DaoSpringConfig;

/**
 * @author mayank
 *
 */

@Configuration
@ComponentScan(basePackages = { "com.companyon.controller", "com.phonepe" })
@Import({ DaoSpringConfig.class })
public class SpringConfig {
}
