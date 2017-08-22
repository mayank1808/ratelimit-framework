/**
 * 
 */
package com.phonepe.ratelimit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.phonepe.ratelimit.interceptor.RatelimitInterceptor;

/**
 * @author mayank
 *
 */
@EnableWebMvc
@Configuration
@PropertySource({ "classpath:memcache.properties" })
public class RatelimitConfig extends WebMvcConfigurerAdapter {

	@Bean
	RatelimitInterceptor getExecuteTimeInterceptor() {
		return new RatelimitInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getExecuteTimeInterceptor());

	}
}
