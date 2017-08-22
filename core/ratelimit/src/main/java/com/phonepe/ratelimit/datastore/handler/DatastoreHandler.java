/**
 * 
 */
package com.phonepe.ratelimit.datastore.handler;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.phonepe.ratelimit.model.Limit;
import com.phonepe.ratelimit.model.RateLimit;
import com.phonepe.ratelimit.model.TimeUnit;
import com.phonepe.ratelimit.response.GenericResponse;

/**
 * @author mayank
 *
 */
public class DatastoreHandler extends AbstractHandler {

	public static Map<String, RateLimit> clientMap = new ConcurrentHashMap<String, RateLimit>();
	public static Map<String, Queue<Long>> timeUnitMap = new ConcurrentHashMap<String, Queue<Long>>();
	public static Map<TimeUnit, Long> timeConversionMap = new EnumMap<TimeUnit, Long>(TimeUnit.class);

	public DatastoreHandler() {
	}

	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String company = request.getHeader("company");
		String method = request.getHeader("method");
		String uri = request.getHeader("uri");

		GenericResponse gr = processAPICall(company, method, uri);
		if (gr != null) {
			if (gr.getCode().equals("200")) {
				response.setStatus(200);
			} else {
				response.setStatus(429);
			}
		}

		System.out.println(gr.toString());
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(Integer.valueOf(gr.getCode()));
		baseRequest.setHandled(true);
		response.getWriter().println("Completed");

		/*		*/

		/*
		 * String jsonString = IOUtils.toString(request.getInputStream());
		 * RateLimitObject myObject = new Gson().fromJson(jsonString,
		 * RateLimitObject.class);
		 * 
		 * RateLimitService.clientMap.put(myObject.getClientId(),
		 * myObject.getRateLimit());
		 */

	}

	public GenericResponse processAPICall(String company, String method, String uri) {

		try {
			RateLimit rateLimit = clientMap.get(company);
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

	private void processClientLimit(String key, Limit limit) throws Exception {
		if (limit != null) {
			for (Entry<TimeUnit, Integer> entryMap : limit.getDurationMap().entrySet()) {

				if (entryMap.getValue() < timeUnitMap.get(key + "_" + entryMap.getKey().toString()).size()) {
					if (System.currentTimeMillis() - timeConversionMap.get(entryMap.getKey()) > timeUnitMap
							.get(key + "_" + entryMap.getKey().toString()).peek()) {
						removeOldEntries(key, entryMap.getKey());
					} else {
						System.out
								.println("Per " + entryMap.getKey().toString() + " rate Limit Exceeded for key " + key);
						throw new Exception(
								"Per " + entryMap.getKey().toString() + " rate Limit Exceeded for key " + key);
					}
				} else {
					try {
						timeUnitMap.get(key + "_" + entryMap.getKey().toString()).add(System.currentTimeMillis());

					} catch (Exception e) {
						removeOldEntries(key, entryMap.getKey());
					}
				}
			}
		}

	}

	private void removeOldEntries(String key, TimeUnit timeunit) throws Exception {

		System.out.println("Started a thread to clear " + timeunit.toString() + " map for " + key);
		Queue<Long> queue = timeUnitMap.get(key + "_" + timeunit.toString());
		boolean flag = false;
		while (!queue.isEmpty() && queue.peek() < System.currentTimeMillis() - timeConversionMap.get(timeunit)) {
			flag = true;
			queue.remove();
		}
		if (!flag)
			throw new Exception("Per " + timeunit.toString() + " rate Limit Exceeded for key " + key);
		else
			timeUnitMap.get(key + "_" + timeunit.toString()).add(System.currentTimeMillis());
	}

}