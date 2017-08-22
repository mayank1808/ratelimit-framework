/**
 * 
 */
package com.phonepe.ratelimit.exception;

/**
 * @author mayank
 *
 */
public class QueueFullException extends Exception {

	private static final long serialVersionUID = -920277427821710715L;
	private String message;

	public QueueFullException() {

	}

	public QueueFullException(String message) {
		super();
		this.message = message;
	}

}
