/**
 * 
 */
package com.companyon.jpa.spring.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author mayank
 *
 */

@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:persistence-mysql.properties" })
public class JPAUtils {

	@Autowired
	private Environment env;
	private final String PACKAGES_TO_SCAN = "com.companyon.repository.entity";

	@Primary
	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(getDataSource());
		em.setPackagesToScan(PACKAGES_TO_SCAN);

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());
		return em;
	}

	@Primary
	@Bean
	@Scope("singleton")
	public DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));

		return dataSource;
	}

	@Primary
	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager() {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {

		return new PersistenceExceptionTranslationPostProcessor();
	}

	public Properties additionalProperties() {
		return new Properties() {
			private static final long serialVersionUID = -4496310006095041101L;

			{

				setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));

				setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));

				setProperty("hibernate.globally_quoted_identifiers",
						env.getProperty("hibernate.globally_quoted_identifiers"));

				setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));

				setProperty("hibernate.bytecode.use_reflection_optimizer",
						env.getProperty("hibernate.bytecode.use_reflection_optimizer"));

				setProperty("cache.provider_class", env.getProperty("cache.provider_class"));

				setProperty("current_session_context_class", env.getProperty("current_session_context_class"));

				setProperty("hibernate.current_session_context_class",
						env.getProperty("hibernate.current_session_context_class"));

			}
		};
	}
}
