/**
 * 
 */
package com.phonepe.ratelimit.model;

/**
 * @author mayank
 *
 */
public class APILimit {

	private String api;
	private Limit limit;

	public APILimit() {

	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public Limit getLimit() {
		return limit;
	}

	public void setLimit(Limit limit) {
		this.limit = limit;
	}

	@Override
	public String toString() {
		return "APILimit [api=" + api + ", limit=" + limit + "]";
	}

}
