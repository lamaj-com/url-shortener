package com.example.urlshortener.controller.exception.custom;

/**
 * 
 * This class represents a custom unchecked exceptions which will be thrown in
 * case shortcode is not valid if it is already taken or if it contains less than 6 chars or contains 
 * forbidden chars
 * 
 * @author mlazic
 * 
 */
public class InvalidShortcodeException extends RuntimeException {

	private static final long serialVersionUID = 1428481994006179779L;

	private String shortcode;
	private String message;
	private ErrorType errorType;

	public InvalidShortcodeException(String shortcode, String message) {
		this.setShortcode(shortcode);
		this.setMessage(message);
	}
	
	public InvalidShortcodeException(String shortcode, ErrorType errorType) {
		this.setShortcode(shortcode);
		this.setMessage(errorType.getMessage());
		this.setErrorType(errorType);
	}

	public InvalidShortcodeException(String shortcode) {
		this.setShortcode(shortcode);
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}

	public enum ErrorType {

		SHORTCODE_ALREADY_IN_USE("Shortcode already in use."), 
		SHORTCODE_NOT_VALID("Shortcode string not valid.");

		String message;

		ErrorType(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

}