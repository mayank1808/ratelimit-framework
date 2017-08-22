/**
 * 
 */
package com.phonepe.ratelimit.service.impl;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.phonepe.ratelimit.cache.IMemcachedService;
import com.phonepe.ratelimit.exception.RateLimitExceededException;
import com.phonepe.ratelimit.model.Limit;
import com.phonepe.ratelimit.model.RateLimit;
import com.phonepe.ratelimit.model.TimeUnit;
import com.phonepe.ratelimit.multithreading.DataCleaner;
import com.phonepe.ratelimit.response.GenericResponse;
import com.phonepe.ratelimit.service.IRateLimitService;

/**
 * @author mayank
 *
 */
@Component
public class RateLimitService implements IRateLimitService {

	private static ExecutorService executor = Executors.newFixedThreadPool(40);

	public static Map<TimeUnit, Long> timeConversionMap = new EnumMap<TimeUnit, Long>(TimeUnit.class);

	@Autowired
	IMemcachedService memCachedService;

	@PostConstruct
	public void initialize() {
		memCachedService.flushAll();

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

		timeConversionMap.put(TimeUnit.SECOND, 1000L);
		timeConversionMap.put(TimeUnit.MINUTE, 60000L);
		timeConversionMap.put(TimeUnit.HOUR, 3600000L);
		timeConversionMap.put(TimeUnit.WEEK, 604800000L);
		timeConversionMap.put(TimeUnit.MONTH, 18144000000L);

	}

	public void addRateLimit(String company, RateLimit rateLimit) {

		Limit limit = rateLimit.getClientLimit();

		if (limit != null && limit.getDurationMap() != null) {
			for (Entry<TimeUnit, Integer> timeUnit : limit.getDurationMap().entrySet()) {
				memCachedService.addOrPut(company + "_" + timeUnit.getKey().toString(),
						new LinkedBlockingQueue<Long>(timeUnit.getValue()));
			}
		}

		if (rateLimit.getLimits() != null) {
			for (Entry<String, Limit> limitMap : rateLimit.getLimits().entrySet()) {
				if (limitMap.getValue().getDurationMap() != null) {
					for (Entry<TimeUnit, Integer> limitMapUnit : limitMap.getValue().getDurationMap().entrySet()) {
						memCachedService.addOrPut(limitMap.getKey() + "_" + limitMapUnit.getKey().toString(),
								new LinkedBlockingQueue<Long>(limitMapUnit.getValue()));
					}
				}
			}
		}

		memCachedService.addOrPut(company, rateLimit);
	}

	public GenericResponse processAPICall(String company, String method, String uri) {

		try {
			RateLimit rateLimit = (RateLimit) memCachedService.get(company);
			if (rateLimit != null) {
				Limit limit = rateLimit.getClientLimit();
				processClientLimit(company, limit);
			}
			Map<String, Limit> limitMap = rateLimit.getLimits();
			if (limitMap != null) {
				if (limitMap.containsKey(company + "_" + method)) {
					processClientLimit(company + "_" + method, limitMap.get(company + "_" + method));
				}
				if (limitMap.containsKey(company + "_" + uri)) {
					processClientLimit(company + "_" + uri, limitMap.get(company + "_" + uri));
				}
			}
		} catch (Exception e) {
			return new GenericResponse("429", e.getMessage());
		}
		return new GenericResponse("200", "Passed");
	}

	@SuppressWarnings("unchecked")
	private void processClientLimit(String key, Limit limit) throws Exception {
		if (limit != null) {
			for (Entry<TimeUnit, Integer> entryMap : limit.getDurationMap().entrySet()) {

				LinkedBlockingQueue<Long> queue = (LinkedBlockingQueue<Long>) memCachedService
						.get(key + "_" + entryMap.getKey().toString());

				if (entryMap.getValue() < queue.size()) {
					if (System.currentTimeMillis() - timeConversionMap.get(entryMap.getKey()) > queue.peek()) {
						removeOldEntries(key, entryMap.getKey());
					} else {
						throw new RateLimitExceededException(
								"Per " + entryMap.getKey().toString() + " rate Limit Exceeded for key " + key);
					}
				} else {
					try {
						queue.add(System.currentTimeMillis());
						memCachedService.replace(key + "_" + entryMap.getKey().toString(), queue);

					} catch (Exception e) {
						removeOldEntries(key, entryMap.getKey());
					}
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void removeOldEntries(String key, TimeUnit timeunit) throws Exception {

		System.out.println("Started a thread to clear " + timeunit.toString() + " map for " + key);
		Future<Boolean> result = executor.submit(new DataCleaner(key, timeunit, memCachedService));
		if (!result.get())
			throw new RateLimitExceededException("Per " + timeunit.toString() + " ratelimit exceeded for key " + key);
	}
}
