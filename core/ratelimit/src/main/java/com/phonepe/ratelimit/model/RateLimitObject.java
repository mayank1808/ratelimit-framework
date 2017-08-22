/**
 * 
 */
package com.phonepe.ratelimit.model;

import java.io.Serializable;

/**
 * @author mayank
 *
 */
public class RateLimitObject implements Serializable {

	private static final long serialVersionUID = -8622493268617663715L;
	private String clientId;
	private RateLimit rateLimit;

	public RateLimitObject(String clientId, RateLimit rateLimit) {
		super();
		this.clientId = clientId;
		this.rateLimit = rateLimit;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public RateLimit getRateLimit() {
		return rateLimit;
	}

	public void setRateLimit(RateLimit rateLimit) {
		this.rateLimit = rateLimit;
	}

	@Override
	public String toString() {
		return "RateLimitObject [clientId=" + clientId + ", rateLimit=" + rateLimit + "]";
	}

}
