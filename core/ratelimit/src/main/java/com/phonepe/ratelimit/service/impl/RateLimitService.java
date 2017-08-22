/**
 * 
 */
package com.phonepe.ratelimit.service.impl;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.phonepe.ratelimit.datastore.handler.DatastoreHandler;
import com.phonepe.ratelimit.datastore.server.DatastoreServer;
import com.phonepe.ratelimit.model.Host;
import com.phonepe.ratelimit.model.Limit;
import com.phonepe.ratelimit.model.RateLimit;
import com.phonepe.ratelimit.model.TimeUnit;
import com.phonepe.ratelimit.request.DatastoreRequest;
import com.phonepe.ratelimit.response.GenericResponse;
import com.phonepe.ratelimit.service.IRateLimitService;

/**
 * @author mayank
 *
 */
@Component
public class RateLimitService implements IRateLimitService {

	public static Set<Host> hosts = new HashSet<Host>();

	private static ExecutorService executor = Executors.newFixedThreadPool(40);

	@PostConstruct
	public void initialize() {
		System.out.println("Initializing");
		String[] args = {};
		try {
			DatastoreServer.main(args);
		} catch (Exception e) {
			System.out.println("oops");
		}

		RateLimit rateLimit = new RateLimit();

		String company = "E-COM";

		Limit limit = new Limit();

		limit.getDurationMap().put(TimeUnit.HOUR, 100);
		limit.getDurationMap().put(TimeUnit.WEEK, 200);
		limit.getDurationMap().put(TimeUnit.MONTH, 10000);

		rateLimit.setClientLimit(limit);

		Limit limit2 = new Limit();

		limit2.getDurationMap().put(TimeUnit.SECOND, 10);
		limit2.getDurationMap().put(TimeUnit.MINUTE, 20);
		limit2.getDurationMap().put(TimeUnit.WEEK, 700);

		rateLimit.getLimits().put(company + "_GET", limit2);

		rateLimit.getLimits().put(company + "_/pay", limit2);

		Limit limit3 = new Limit();

		limit3.getDurationMap().put(TimeUnit.SECOND, 20);
		limit3.getDurationMap().put(TimeUnit.HOUR, 60);
		limit3.getDurationMap().put(TimeUnit.WEEK, 900);
		limit3.getDurationMap().put(TimeUnit.MONTH, 1000);

		rateLimit.getLimits().put(company + "_POST", limit3);

		rateLimit.getLimits().put(company + "_/status", limit3);

		addRateLimit(company, rateLimit);

		DatastoreHandler.timeConversionMap.put(TimeUnit.SECOND, 1000L);
		DatastoreHandler.timeConversionMap.put(TimeUnit.MINUTE, 60000L);
		DatastoreHandler.timeConversionMap.put(TimeUnit.HOUR, 3600000L);
		DatastoreHandler.timeConversionMap.put(TimeUnit.WEEK, 604800000L);
		DatastoreHandler.timeConversionMap.put(TimeUnit.MONTH, 18144000000L);

		if (hosts.size() == 0) {
			hosts.add(new Host("http", "127.0.0.1", "3535"));
		}

	}

	public void addRateLimit(String company, RateLimit rateLimit) {

		Limit limit = rateLimit.getClientLimit();
		if (limit != null && limit.getDurationMap() != null) {
			for (Entry<TimeUnit, Integer> timeUnit : limit.getDurationMap().entrySet()) {
				DatastoreHandler.timeUnitMap.put(company + "_" + timeUnit.getKey().toString(),
						new LinkedBlockingQueue<Long>(timeUnit.getValue()));
			}
		}

		if (rateLimit.getLimits() != null) {
			for (Entry<String, Limit> limitMap : rateLimit.getLimits().entrySet()) {
				if (limitMap.getValue().getDurationMap() != null) {
					for (Entry<TimeUnit, Integer> limitMapUnit : limitMap.getValue().getDurationMap().entrySet()) {
						DatastoreHandler.timeUnitMap.put(limitMap.getKey() + "_" + limitMapUnit.getKey().toString(),
								new LinkedBlockingQueue<Long>(limitMapUnit.getValue()));
					}
				}
			}
		}

		DatastoreHandler.clientMap.put(company, rateLimit);

	}

	public GenericResponse processAPICall(String company, String method, String uri)
			throws InterruptedException, ExecutionException {
		Callable<GenericResponse> callable = new DatastoreRequest(company, method, uri, hosts);
		Future<GenericResponse> future = executor.submit(callable);
		return future.get();
	}
}
