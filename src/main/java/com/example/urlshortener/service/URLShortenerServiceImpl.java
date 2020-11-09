package com.example.urlshortener.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.urlshortener.controller.exception.custom.InvalidShortcodeException;
import com.example.urlshortener.controller.exception.custom.InvalidShortcodeException.ErrorType;
import com.example.urlshortener.controller.exception.custom.InvalidUrlException;
import com.example.urlshortener.controller.exception.custom.ShortcodeNotFoundException;
import com.example.urlshortener.controller.rest.URLShortenerController;
import com.example.urlshortener.model.TinyURL;
import com.example.urlshortener.repository.TinyURLRepository;

@Service
public class URLShortenerServiceImpl implements URLShortenerService {

	private TinyURLRepository tinyUrlRepository;

	private AccessLogService accessLogService;

	private ShortcodeGenerator shortcodeGenerator;

	@Value("${spring.application.url-shortener-alg-type}")
	private String urlShortenerAlgType;

	@Value("${spring.application.shortcode-length}")
	private int shortcodeLength;

	private final static Logger LOGGER = LoggerFactory.getLogger(URLShortenerController.class);

	@Autowired
	public URLShortenerServiceImpl(TinyURLRepository tinyUrlRepository, AccessLogService accessLogService,
			ShortcodeGenerator base64Conversion) {
		this.tinyUrlRepository = tinyUrlRepository;
		this.accessLogService = accessLogService;
		this.shortcodeGenerator = base64Conversion;
	}

	public TinyURL saveURL(TinyURL url) {
		// URL validation
		if (StringUtils.isEmpty(url.getUrl())) {
			LOGGER.error("Url not present.");
			throw new InvalidUrlException("Url not present (required).");
		}
		UrlValidator urlValidator = UrlValidator.getInstance();
		if (!urlValidator.isValid(url.getUrl())) {
			throw new InvalidUrlException("Url not valid.");
		}

		// Shortcode validation
		String shortcode = url.getShortcode();
		if (!StringUtils.isEmpty(shortcode)) {
			if (!this.isShortcodeValid(url.getShortcode())) {
				throw new InvalidShortcodeException(url.getShortcode(), ErrorType.SHORTCODE_NOT_VALID);
			}
			if (tinyUrlRepository.getByShortcode(url.getShortcode()) != null) {
				throw new InvalidShortcodeException(url.getShortcode(), ErrorType.SHORTCODE_ALREADY_IN_USE);
			}
		}
		if (StringUtils.isEmpty(shortcode)) {
			TinyURL existingUrl = this.findOneByLongUrl(url.getUrl());
			if (existingUrl != null) {
				url.setShortcode(existingUrl.getShortcode());
				return url;
			} else {
				url = tinyUrlRepository.save(url); // to obtain the id
				shortcode = this.generateShortcode(url, URLShortenerAlgType.valueOf(this.urlShortenerAlgType));
			}
			url.setShortcode(shortcode);

		}
		return tinyUrlRepository.save(url);
	}

	/**
	 * Creates a shortcode for a long url.
	 *
	 * 
	 * @param {@link URLShortenerAlgType} urlShortenerAlgType algorithm used for
	 *               generating shortcode
	 * @return shortcode
	 */
	private String generateShortcode(TinyURL url, URLShortenerAlgType urlShortenerAlgType) {
		String shortcode = null;
		if (URLShortenerAlgType.UNIQUE_ID_WITH_BASE_64.equals(urlShortenerAlgType)) {
			shortcode = shortcodeGenerator.generateShortcodeUsingId(url.getId());
		} else if (URLShortenerAlgType.RANDOM_WITH_FIXED_LENGTH.equals(urlShortenerAlgType)) {
			while (true) {
				shortcode = shortcodeGenerator.generateRandomShortcode(this.shortcodeLength);
				if (tinyUrlRepository.getByShortcode(shortcode) == null) {
					url.setShortcode(shortcode);
					shortcode = url.getShortcode();
					break;
				}
			}
		}

		return shortcode;
	}

	/**
	 * Finds one TinyUrl based on given long url.
	 *
	 * 
	 * @param url long url
	 * @return {@link TinyUrl}
	 */
	private TinyURL findOneByLongUrl(String url) {
		Optional<TinyURL> tinyUrl = tinyUrlRepository.findTopByUrl(url);
		if (tinyUrl.isPresent()) {
			return tinyUrl.get();
		} else
			return null;
	}

	public boolean isShortcodeValid(String shortcode) {
		return (shortcode.length() == this.shortcodeLength && (shortcode.matches("^[a-zA-Z0-9_]*$")));
	}

	@Transactional
	public TinyURL getURLByShortcode(String shortcode, boolean disableStats) {
		TinyURL url = tinyUrlRepository.getByShortcode(shortcode);
		if (url != null) {
			if (!disableStats) {
				accessLogService.saveAccessLogRecord(url);
			}
			return url;
		} else
			throw new ShortcodeNotFoundException(shortcode, "Shortcode not found.");
	}

	public enum URLShortenerAlgType {

		UNIQUE_ID_WITH_BASE_64, RANDOM_WITH_FIXED_LENGTH

	}

}
