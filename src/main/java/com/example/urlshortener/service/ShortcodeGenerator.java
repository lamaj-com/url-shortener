package com.example.urlshortener.service;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class ShortcodeGenerator {

	private final static char allowedChars_63[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_"
			.toCharArray();

	/**
	 * Creates a shortcode for an url. The shortcode will be of specified length and
	 * it will contain only alphanumeric characters or underscores. Be aware that
	 * collisions could happen and that are other better solutions.
	 *
	 * 
	 * @param length shortcode length
	 * @return created shortcode
	 */
	public String generateRandomShortcode(int length) {
		StringBuilder shortcode = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int randomIndex = ThreadLocalRandom.current().nextInt(0, allowedChars_63.length);
			shortcode.append(allowedChars_63[randomIndex]);
		}
		return shortcode.toString();
	}

	/**
	 * Generates a shortcode. Uses the auto incremented id stored in the database
	 * and converts it to a character string.
	 *
	 * 
	 * @param id auto incremented id
	 * @return created shortcode
	 */
	public String generateShortcodeUsingId(long id) {
		StringBuilder shortcode = new StringBuilder();
		// Convert given integer id to a base 62 number
		while (id > 0) {
			shortcode.append(allowedChars_63[(int) (id % allowedChars_63.length)]);
			id = id / allowedChars_63.length;
		}
		return shortcode.reverse().toString();
	}

	/**
	 * Converts shortcode back to id
	 *
	 * 
	 * @param length shortcode length
	 * @return created shortcode
	 */
	public long convertShortURLtoID(String shortcode) {
		long id = 0;
		for (int i = 0; i < shortcode.length(); i++) {
			if ('a' <= shortcode.charAt(i) && shortcode.charAt(i) <= 'z')
				id = id * allowedChars_63.length + shortcode.charAt(i) - 'a';
			if ('A' <= shortcode.charAt(i) && shortcode.charAt(i) <= 'Z')
				id = id * allowedChars_63.length + shortcode.charAt(i) - 'A' + 26;
			if ('0' <= shortcode.charAt(i) && shortcode.charAt(i) <= '9')
				id = id * allowedChars_63.length + shortcode.charAt(i) - '0' + 52;
			if ('_' == shortcode.charAt(i))
				id = id * allowedChars_63.length + shortcode.charAt(i) - '_' + 62;
		}
		return id;
	}

}
