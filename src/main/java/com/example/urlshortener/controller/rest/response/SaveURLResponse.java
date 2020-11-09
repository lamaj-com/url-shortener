package com.example.urlshortener.controller.rest.response;

/**
 * 
 * This class represents a response from SaveUrl service
 * 
 * @author mlazic
 * 
 */
public class SaveURLResponse {
	
	private String shortcode;

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortCode) {
		this.shortcode = shortCode;
	}
}
