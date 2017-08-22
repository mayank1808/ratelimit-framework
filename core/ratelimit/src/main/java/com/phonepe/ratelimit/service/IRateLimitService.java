/**
 * 
 */
package com.phonepe.ratelimit.service;

import java.util.concurrent.ExecutionException;

import com.phonepe.ratelimit.model.RateLimit;
import com.phonepe.ratelimit.response.GenericResponse;

/**
 * @author mayank
 *
 */

public interface IRateLimitService {
	public void initialize();

	public void addRateLimit(String company, RateLimit rateLimit);

	public GenericResponse processAPICall(String company, String method, String uri) throws InterruptedException, ExecutionException;

}