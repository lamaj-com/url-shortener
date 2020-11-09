package com.example.urlshortener.controller.exception.custom;

/**
 * 
 * This class represents a custom unchecked exceptions which will be thrown in
 * case URL is not valid
 * 
 * @author mlazic
 * 
 */
public class InvalidUrlException extends RuntimeException {

	private static final long serialVersionUID = 1428481994006179779L;

	private String url;
	private String message;

	public InvalidUrlException(String url, String message) {
		this.setUrl(url);
		this.setMessage(message);
	}

	public InvalidUrlException(String message) {
		this.setMessage(message);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



}