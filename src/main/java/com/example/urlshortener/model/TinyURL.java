package com.example.urlshortener.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class TinyURL {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Lob
	private String url;
	private String shortcode;

	public TinyURL() {
		super();
	}

	public TinyURL(String url, String shortcode) {
		super();
		this.url = url;
		this.shortcode = shortcode;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TinyURL [id=");
		builder.append(id);
		builder.append(", url=");
		builder.append(url);
		builder.append(", shortcode=");
		builder.append(shortcode);
		builder.append("]");
		return builder.toString();
	}

}
