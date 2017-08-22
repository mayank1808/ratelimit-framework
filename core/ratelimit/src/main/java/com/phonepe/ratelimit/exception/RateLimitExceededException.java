/**
 * 
 */
package com.phonepe.ratelimit.exception;

/**
 * @author mayank
 *
 */
public class RateLimitExceededException extends Exception {

	private static final long serialVersionUID = 5319526693243710587L;

	private String message;

	public RateLimitExceededException() {

	}

	public RateLimitExceededException(String message) {
		super();
		this.message = message;
	}

}
