/**
 * 
 */
package com.phonepe.ratelimit.model;

/**
 * @author mayank
 *
 */
public class MethodLimit {

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
