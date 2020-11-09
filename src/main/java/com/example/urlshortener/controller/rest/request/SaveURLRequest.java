package com.example.urlshortener.controller.rest.request;

/**
 * 
 * This class represents a save url web service request
 * 
 * @author mlazic
 * 
 */
public class SaveURLRequest {

	private String url;
	private String shortcode;

	public SaveURLRequest() {
		super();
	}

	public SaveURLRequest(String url, String shortcode) {
		super();
		this.url = url;
		this.shortcode = shortcode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SaveURLRequest [url=");
		builder.append(url);
		builder.append(", shortcode=");
		builder.append(shortcode);
		builder.append("]");
		return builder.toString();
	}

}
