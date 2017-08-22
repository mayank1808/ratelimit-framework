/**
 * 
 */
package com.phonepe.ratelimit.service.impl;

import java.util.HashSet;
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

		DatastoreHandler.timeUnitMap.put(company + "_" + TimeUnit.HOUR.toString(), new LinkedBlockingQueue<Long>(100));
		DatastoreHandler.timeUnitMap.put(company + "_" + TimeUnit.WEEK.toString(), new LinkedBlockingQueue<Long>(200));
		DatastoreHandler.timeUnitMap.put(company + "_" + TimeUnit.MONTH.toString(),
				new LinkedBlockingQueue<Long>(10000));

		Limit limit2 = new Limit();

		limit2.getDurationMap().put(TimeUnit.SECOND, 10);
		limit2.getDurationMap().put(TimeUnit.MINUTE, 20);
		limit2.getDurationMap().put(TimeUnit.WEEK, 700);

		rateLimit.getLimits().put(company + "_GET", limit2);

		DatastoreHandler.timeUnitMap.put(company + "_GET_" + TimeUnit.SECOND.toString(),
				new LinkedBlockingQueue<Long>(10));
		DatastoreHandler.timeUnitMap.put(company + "_GET_" + TimeUnit.MINUTE.toString(),
				new LinkedBlockingQueue<Long>(20));
		DatastoreHandler.timeUnitMap.put(company + "_GET_" + TimeUnit.WEEK.toString(),
				new LinkedBlockingQueue<Long>(700));

		rateLimit.getLimits().put(company + "_/pay", limit2);

		DatastoreHandler.timeUnitMap.put(company + "_/pay_" + TimeUnit.SECOND.toString(),
				new LinkedBlockingQueue<Long>(10));
		DatastoreHandler.timeUnitMap.put(company + "_/pay_" + TimeUnit.MINUTE.toString(),
				new LinkedBlockingQueue<Long>(20));
		DatastoreHandler.timeUnitMap.put(company + "_/pay_" + TimeUnit.WEEK.toString(),
				new LinkedBlockingQueue<Long>(700));

		Limit limit3 = new Limit();

		limit3.getDurationMap().put(TimeUnit.SECOND, 20);
		limit3.getDurationMap().put(TimeUnit.HOUR, 60);
		limit3.getDurationMap().put(TimeUnit.WEEK, 900);
		limit3.getDurationMap().put(TimeUnit.MONTH, 1000);

		rateLimit.getLimits().put(company + "_POST", limit3);

		DatastoreHandler.timeUnitMap.put(company + "_POST_" + TimeUnit.SECOND.toString(),
				new LinkedBlockingQueue<Long>(20));
		DatastoreHandler.timeUnitMap.put(company + "_POST_" + TimeUnit.HOUR.toString(),
				new LinkedBlockingQueue<Long>(60));
		DatastoreHandler.timeUnitMap.put(company + "_POST_" + TimeUnit.WEEK.toString(),
				new LinkedBlockingQueue<Long>(900));
		DatastoreHandler.timeUnitMap.put(company + "_POST_" + TimeUnit.MONTH.toString(),
				new LinkedBlockingQueue<Long>(1000));

		rateLimit.getLimits().put(company + "_/status", limit3);

		DatastoreHandler.timeUnitMap.put(company + "_/status_" + TimeUnit.SECOND.toString(),
				new LinkedBlockingQueue<Long>(20));
		DatastoreHandler.timeUnitMap.put(company + "_/status_" + TimeUnit.HOUR.toString(),
				new LinkedBlockingQueue<Long>(60));
		DatastoreHandler.timeUnitMap.put(company + "_/status_" + TimeUnit.WEEK.toString(),
				new LinkedBlockingQueue<Long>(900));
		DatastoreHandler.timeUnitMap.put(company + "_/status_" + TimeUnit.MONTH.toString(),
				new LinkedBlockingQueue<Long>(1000));

		DatastoreHandler.clientMap.put(company, rateLimit);

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

	}

	public GenericResponse processAPICall(String company, String method, String uri)
			throws InterruptedException, ExecutionException {
		System.out.println("processing API call");
		Callable<GenericResponse> callable = new DatastoreRequest(company, method, uri, hosts);
		Future<GenericResponse> future = executor.submit(callable);
		return future.get();
	}
}
