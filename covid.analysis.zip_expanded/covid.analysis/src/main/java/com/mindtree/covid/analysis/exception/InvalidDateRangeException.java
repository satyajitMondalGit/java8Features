package com.mindtree.covid.analysis.exception;

public class InvalidDateRangeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5866413827701489268L;

	public InvalidDateRangeException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public InvalidDateRangeException(String message) {
		super(message);
	}

	
}
