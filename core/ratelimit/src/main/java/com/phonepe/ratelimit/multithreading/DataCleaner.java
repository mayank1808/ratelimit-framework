/**
 * 
 */
package com.phonepe.ratelimit.multithreading;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

import com.phonepe.ratelimit.cache.IMemcachedService;
import com.phonepe.ratelimit.model.TimeUnit;
import com.phonepe.ratelimit.service.impl.RateLimitService;

/**
 * @author mayank
 *
 */
public class DataCleaner implements Callable<Boolean> {

	private String key;
	private TimeUnit timeunit;
	private IMemcachedService memCachedService;

	public DataCleaner() {

	}

	public DataCleaner(String key, TimeUnit timeunit, IMemcachedService memCachedService) {
		super();
		this.key = key;
		this.timeunit = timeunit;
		this.memCachedService = memCachedService;
	}

	public Boolean call() throws Exception {
		LinkedBlockingQueue<Long> queue = (LinkedBlockingQueue<Long>) memCachedService
				.get(key + "_" + timeunit.toString());
		boolean flag = false;
		while (!queue.isEmpty()
				&& queue.peek() < System.currentTimeMillis() - RateLimitService.timeConversionMap.get(timeunit)) {
			flag = true;
			queue.remove();
		}
		if (!flag)
			return false;
		else {
			queue.add(System.currentTimeMillis());
			memCachedService.replace(key + "_" + timeunit.toString(), queue);
		}
		return true;
	}

}
