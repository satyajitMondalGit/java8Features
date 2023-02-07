package com.mindtree.covid.analysis.exception;

public class InvalidStateCodeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String msg;
	
	public InvalidStateCodeException(String msg) {
		super(msg);
		this.msg = msg;
	}
}
