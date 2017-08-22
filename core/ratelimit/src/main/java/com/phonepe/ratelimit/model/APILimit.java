/**
 * 
 */
package com.phonepe.ratelimit.model;

import java.io.Serializable;

/**
 * @author mayank
 *
 */
public class APILimit implements Serializable {

	private static final long serialVersionUID = -3022228115797149301L;
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
