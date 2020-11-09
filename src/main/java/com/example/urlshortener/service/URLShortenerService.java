package com.example.urlshortener.service;

import com.example.urlshortener.controller.exception.custom.InvalidShortcodeException;
import com.example.urlshortener.model.TinyURL;

public interface URLShortenerService {

	/**
	 * Creates a tiny url for a long url based on the specified algorithm
	 *
	 * 
	 * @param {@link TinyURL} shortcode for the given url
	 * @return saved {@link TinyURL}
	 * @throws InvalidShortcodeException, InvalidUrlException
	 */
	TinyURL saveURL(TinyURL url);

	/**
	 * Retrieves tinyUrl for the specified shortcode and records the access log
	 * entry
	 *
	 * 
	 * @param {@link TinyURL} shortcode for the given url
	 * @return {@link TinyURL}
	 * @throws ShortcodeNotFoundException
	 */
	TinyURL getURLByShortcode(String shortcode, boolean disableStats);
	
	/**
	 * Checks whether shortcode has valid length and whether contains only
	 * alphanumeric characters and underscores
	 *
	 * 
	 * @param shortcode
	 * @return true if valid, false otherwise
	 */
	public boolean isShortcodeValid(String shortcode);

}
