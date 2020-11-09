package com.example.urlshortener.controller.rest;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.urlshortener.controller.rest.dto.RedirectStatsDTO;
import com.example.urlshortener.controller.rest.request.SaveURLRequest;
import com.example.urlshortener.controller.rest.response.GetUsageStatisticsResponse;
import com.example.urlshortener.controller.rest.response.SaveURLResponse;
import com.example.urlshortener.model.RedirectStatsRecord;
import com.example.urlshortener.model.TinyURL;
import com.example.urlshortener.service.URLShortenerService;
import com.example.urlshortener.service.AccessLogService;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/url")
public class URLShortenerController {

	private ModelMapper modelMapper;
	private URLShortenerService urlShortenerService;
	private AccessLogService usageStatsService;

	private final static Logger LOGGER = LoggerFactory.getLogger(URLShortenerController.class);

	@Autowired
	public URLShortenerController(ModelMapper modelMapper, URLShortenerService urlShortenerService,
			AccessLogService usageStatsService) {
		this.modelMapper = modelMapper;
		this.urlShortenerService = urlShortenerService;
		this.usageStatsService = usageStatsService;
	}

	@PostMapping(consumes = "application/json", produces = "application/json")
	@ApiOperation(value = "Saves shortcode for the given url", response = SaveURLResponse.class, consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Shortcode for the given url saved.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaveURLResponse.class))),
			@ApiResponse(responseCode = "400", description = "Url not present (required)."),
			@ApiResponse(responseCode = "409", description = "Shortcode already in use.") })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<SaveURLResponse> saveURL(@RequestBody(required = true) SaveURLRequest request) {
		LOGGER.debug("SaveURL - START: {}", request);
		TinyURL urlToBeSaved = modelMapper.map(request, TinyURL.class);
		TinyURL savedUrl = urlShortenerService.saveURL(urlToBeSaved);
		SaveURLResponse response = modelMapper.map(savedUrl, SaveURLResponse.class);
		LOGGER.debug("SaveURL - END");
		return new ResponseEntity<SaveURLResponse>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{shortcode}")
	@ApiOperation(value = "Retrieves the original URL by specified shortcode and redirects.", response = SaveURLResponse.class)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "302", description = "Redirection to original URL location", headers = {
					@Header(name = "location") }),
			@ApiResponse(responseCode = "404", description = "Shortcode not found.") })
	@ResponseStatus(HttpStatus.FOUND)
	@Cacheable(value = "urls", key = "#shortcode", sync = true)
	public ResponseEntity<Void> getURLByShortcode(@PathVariable String shortcode) {
		LOGGER.debug("GetURLByShortcodeResponse - START");
		TinyURL url = urlShortenerService.getURLByShortcode(shortcode, false);
		LOGGER.debug("GetURLByShortcodeResponse - END");
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url.getUrl())).build();
	}

	@GetMapping(path = "/{shortcode}/usage-stats", produces = "application/json")
	@ApiOperation(value = "Retrieves the usage statistics for the given shortcode in the given time span", response = GetUsageStatisticsResponse.class)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usage statistics for the given shortcode.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetUsageStatisticsResponse.class))),
			@ApiResponse(responseCode = "404", description = "Shortcode not found.") })
	public ResponseEntity<GetUsageStatisticsResponse> getUsageStatistics(@PathVariable String shortcode,
			@RequestParam(name = "startYear", required = true) int startYear,
			@RequestParam(name = "endYear", required = true) int endYear) {
		LOGGER.debug("GetUsageStatisticsResponse - START");
		TinyURL url = urlShortenerService.getURLByShortcode(shortcode, true);
		List<RedirectStatsRecord> stats = usageStatsService.getShortcodeUsageStatiscs(startYear, endYear, url.getShortcode());
		List<RedirectStatsDTO> response = stats.stream().map(stat -> modelMapper.map(stat, RedirectStatsDTO.class))
				.collect(Collectors.toList());
		LOGGER.debug("GetUsageStatisticsResponse - END");
		return new ResponseEntity<GetUsageStatisticsResponse>(new GetUsageStatisticsResponse(response), HttpStatus.OK);
	}

}
