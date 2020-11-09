package com.example.urlshortener.controller.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.urlshortener.controller.exception.custom.CustomException;
import com.example.urlshortener.controller.exception.custom.InvalidShortcodeException;
import com.example.urlshortener.controller.exception.custom.InvalidUrlException;
import com.example.urlshortener.controller.exception.custom.ShortcodeNotFoundException;

/**
 * This class is used for global handling of custom exceptions.
 * 
 * @author mlazic
 * @since 1.0
 * 
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(InvalidShortcodeException.class)
	public ResponseEntity<CustomException> handleInvalidShortcodeException(InvalidShortcodeException ex,
			WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		CustomException exception = new CustomException("Invalid shortcode: '" + ex.getShortcode() + "'",
				ex.getMessage());
		if (InvalidShortcodeException.ErrorType.SHORTCODE_ALREADY_IN_USE.equals(ex.getErrorType())) {
			return new ResponseEntity<CustomException>(exception, headers, HttpStatus.CONFLICT);
		}
		if (InvalidShortcodeException.ErrorType.SHORTCODE_NOT_VALID.equals(ex.getErrorType())) {
			return new ResponseEntity<CustomException>(exception, headers, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<CustomException>(exception, headers, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ShortcodeNotFoundException.class)
	public ResponseEntity<CustomException> handleShortcodeNotFoundException(ShortcodeNotFoundException ex,
			WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		CustomException exception = new CustomException("Shortcode '" + ex.getShortcode() + "' not found.",
				ex.getMessage());
		return new ResponseEntity<CustomException>(exception, headers, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidUrlException.class)
	public ResponseEntity<CustomException> handleShortcodeNotFoundException(InvalidUrlException ex,
			WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		CustomException exception = new CustomException("Invalid url.", ex.getMessage());
		return new ResponseEntity<CustomException>(exception, headers, HttpStatus.BAD_REQUEST);
	}

}
