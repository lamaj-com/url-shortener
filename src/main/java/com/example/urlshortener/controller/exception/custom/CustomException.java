package com.example.urlshortener.controller.exception.custom;

/**
 * This class represents a JSON response to a problem, e.g. an error.
 *
 */
public class CustomException {

	/**
	 * A human readable explanation specific to this occurrence of the problem
	 * (suited for non technical people).
	 *
	 */
	private String message;

	/**
	 * A summary of the problem (usually string returned from
	 * {@link Exception#getMessage()}) Written in English and readable for engineers
	 * (usually not suited for non technical people)
	 *
	 */
	private String debugMessage;
	
	

	public CustomException(String message, String debugMessage) {
		super();
		this.message = message;
		this.debugMessage = debugMessage;
	}

	public void setMessage(String message) {
		this.message = message;

	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

	public String getMessage() {
		return message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

}
