/**
 * 
 */
package com.phonepe.ratelimit.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mayank
 *
 */
public class RateLimit implements Serializable {

	private static final long serialVersionUID = -5215707323537640306L;

	private Limit clientLimit;

	private Map<String, Limit> limits;

	public RateLimit() {
		limits = new HashMap<String, Limit>();
	}

	public Limit getClientLimit() {
		return clientLimit;
	}

	public void setClientLimit(Limit clientLimit) {
		this.clientLimit = clientLimit;
	}

	public Map<String, Limit> getLimits() {
		return limits;
	}

	public void setLimits(Map<String, Limit> limits) {
		this.limits = limits;
	}

	@Override
	public String toString() {
		return "RateLimit [clientLimit=" + clientLimit + ", limits=" + limits + "]";
	}

}
