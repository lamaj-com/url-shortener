package com.example.urlshortener.controller.exception.custom;

/**
 * 
 * This class represents a custom unchecked exceptions which will be thrown in
 * case shortcode does not exist
 * 
 * @author mlazic
 * 
 */
public class ShortcodeNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1428481994006179779L;

	private String shortcode;
	private String message;

	public ShortcodeNotFoundException(String shortcode, String message) {
		this.setShortcode(shortcode);
		this.setMessage(message);
	}


	public ShortcodeNotFoundException(String shortcode) {
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

}
