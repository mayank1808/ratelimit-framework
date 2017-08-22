/**
 * 
 */
package com.phonepe.ratelimit.model;

import java.io.Serializable;

/**
 * @author mayank
 *
 */
public class MethodLimit implements Serializable {

	private static final long serialVersionUID = -2350822971548157209L;
	private Method method;
	private Limit limit;

	public MethodLimit() {

	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Limit getLimit() {
		return limit;
	}

	public void setLimit(Limit limit) {
		this.limit = limit;
	}

	@Override
	public String toString() {
		return "MethodLimit [method=" + method + ", limit=" + limit + "]";
	}

}
