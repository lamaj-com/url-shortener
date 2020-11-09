package com.example.urlshortener.controller.rest;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.Locale;

import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.urlshortener.URLShortenerApplication;
import com.example.urlshortener.controller.exception.custom.CustomException;
import com.example.urlshortener.controller.rest.dto.RedirectStatsDTO;
import com.example.urlshortener.controller.rest.request.SaveURLRequest;
import com.example.urlshortener.controller.rest.response.GetUsageStatisticsResponse;
import com.example.urlshortener.controller.rest.response.SaveURLResponse;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { PersistenceContext.class, URLShortenerApplication.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class URLShortenerControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	int port;

	@Test
	public void testSaveUrl_ok_with_shortcode() {
		SaveURLRequest request = new SaveURLRequest("https://google.com/", "45UFLS");
		ResponseEntity<SaveURLResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/url",
				request, SaveURLResponse.class);

		assertNotNull(response.getBody());
		assertEquals("45UFLS", response.getBody().getShortcode());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

	}

	@Test
	public void testSaveUrl_ok_without_shortcode() {
		SaveURLRequest request = new SaveURLRequest(
				"https://www.google.com/search?newwindow=1&client=firefox-b-d&sxsrf=ALeKk00ByD33wRdbgN21W6ioE2ZPifn-BQ%3A1604824960416&ei=gK-nX4P5GMOakwXnhpGYDw&q=url+shortener+implementation+java+include+underscore&oq=url+shortener+implementation+java+include+underscore&gs_lcp=CgZwc3ktYWIQAzIFCCEQoAEyBAghEBU6BwghEAoQoAFQ4ztYr0xg6U1oAHAAeACAAcwEiAHwGZIBCzAuMy41LjEuMS4xmAEAoAEBqgEHZ3dzLXdpesABAQ&sclient=psy-ab&ved=0ahUKEwiDucTtxvLsAhVDzaQKHWdDBPMQ4dUDCAw&uact=5",
				"");
		ResponseEntity<SaveURLResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/url",
				request, SaveURLResponse.class);

		assertNotNull(response.getBody());
		assertFalse(response.getBody().getShortcode().isEmpty());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testSaveUrl_nok_shortcode_is_already_in_use() {
		SaveURLRequest request = new SaveURLRequest("https://google.com/", "TEST_9");
		ResponseEntity<SaveURLResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/url",
				request, SaveURLResponse.class);

		assertEquals("TEST_9", response.getBody().getShortcode());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		SaveURLRequest request2 = new SaveURLRequest("https://youtube.com/", "TEST_9");
		ResponseEntity<CustomException> response2 = restTemplate.postForEntity("http://localhost:" + port + "/url",
				request2, CustomException.class);

		assertEquals("Shortcode already in use.", response2.getBody().getDebugMessage());
		assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());
	}

	@Test
	public void testSaveUrl_nok_url_is_not_present() {
		SaveURLRequest request = new SaveURLRequest("", "TEST_9");
		ResponseEntity<CustomException> response = restTemplate.postForEntity("http://localhost:" + port + "/url",
				request, CustomException.class);

		assertEquals("Url not present (required).", response.getBody().getDebugMessage());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testSaveUrl_nok_url_is_not_valid() {
		SaveURLRequest request = new SaveURLRequest("javascript:test", "TEST_9");
		ResponseEntity<CustomException> response = restTemplate.postForEntity("http://localhost:" + port + "/url",
				request, CustomException.class);

		assertEquals("Url not valid.", response.getBody().getDebugMessage());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testSaveUrl_nok_shortcode_is_not_valid() {
		SaveURLRequest request = new SaveURLRequest("https://google.com", "TEST9"); // 5 chars shortcode
		ResponseEntity<CustomException> response = restTemplate.postForEntity("http://localhost:" + port + "/url",
				request, CustomException.class);

		assertEquals("Invalid shortcode: 'TEST9'", response.getBody().getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testSaveUrl_ok_url_existing_shortcode_not_provided() {
		SaveURLRequest request = new SaveURLRequest("https://google.com", "TEST_9");
		ResponseEntity<SaveURLResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/url",
				request, SaveURLResponse.class);

		assertNotNull(response.getBody());
		assertEquals("TEST_9", response.getBody().getShortcode());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		SaveURLRequest request2 = new SaveURLRequest("https://google.com", "");
		ResponseEntity<SaveURLResponse> response2 = restTemplate.postForEntity("http://localhost:" + port + "/url",
				request2, SaveURLResponse.class);

		assertNotNull(response2.getBody());
		assertEquals("TEST_9", response2.getBody().getShortcode());
		assertEquals(HttpStatus.CREATED, response2.getStatusCode());
	}

	@Test
	public void testGetByShortcode_ok() throws URISyntaxException {
		SaveURLRequest request = new SaveURLRequest("https://google.com", "TEST_9");
		restTemplate.postForEntity("http://localhost:" + port + "/url", request, SaveURLResponse.class);

		ResponseEntity<Void> responseGet = restTemplate
				.getForEntity("http://localhost:" + port + "/url" + "/TEST_9", Void.class);

		assertEquals(new URI("https://google.com"), responseGet.getHeaders().getLocation());
		assertEquals(HttpStatus.FOUND, responseGet.getStatusCode());
	}

	@Test
	public void testGetByShortcode_nok_shortcode_not_found() throws URISyntaxException {
		ResponseEntity<CustomException> responseGet = restTemplate
				.getForEntity("http://localhost:" + port + "/url" + "/TEST_9", CustomException.class);
		assertEquals("Shortcode not found.", responseGet.getBody().getDebugMessage());
		assertEquals("Shortcode 'TEST_9' not found.", responseGet.getBody().getMessage());
		assertEquals(HttpStatus.NOT_FOUND, responseGet.getStatusCode());
	}

	@Test
	public void testGetUsageStats_ok() throws URISyntaxException {
		SaveURLRequest request = new SaveURLRequest("https://google.com", "TEST_9");
		restTemplate.postForEntity("http://localhost:" + port + "/url", request, SaveURLResponse.class);

		restTemplate.getForEntity("http://localhost:" + port + "/url" + "/TEST_9", Void.class);
		restTemplate.getForEntity("http://localhost:" + port + "/url" + "/TEST_9", Void.class);
		restTemplate.getForEntity("http://localhost:" + port + "/url" + "/TEST_9", Void.class);

		ResponseEntity<GetUsageStatisticsResponse> responseGet = restTemplate.getForEntity(
				"http://localhost:" + port + "/url" + "/TEST_9/usage-stats?startYear=2020&endYear=2020",
				GetUsageStatisticsResponse.class);

		LocalDate date = LocalDate.now(ZoneId.of("Europe/Berlin"));
		int weekOfYear = date.get(WeekFields.of(Locale.GERMANY).weekOfYear());

		RedirectStatsDTO redirectStatsDto = new RedirectStatsDTO(date.getYear(), weekOfYear, 3);
		assertEquals(1, responseGet.getBody().getResult().size());
		assertEquals(redirectStatsDto.getRedirect_counts(),
				responseGet.getBody().getResult().get(0).getRedirect_counts());
		assertEquals(redirectStatsDto.getWeekNumber(), responseGet.getBody().getResult().get(0).getWeekNumber());
		assertEquals(redirectStatsDto.getYear(), responseGet.getBody().getResult().get(0).getYear());

	}

	@Test
	public void testGetUsageStats_nok_shortcode_invalid() throws URISyntaxException {

		ResponseEntity<CustomException> responseGet = restTemplate.getForEntity(
				"http://localhost:" + port + "/url" + "/TEST_9/usage-stats?startYear=2020&endYear=2020",
				CustomException.class);

		assertEquals("Shortcode not found.", responseGet.getBody().getDebugMessage());
		assertEquals("Shortcode 'TEST_9' not found.", responseGet.getBody().getMessage());
		assertEquals(HttpStatus.NOT_FOUND, responseGet.getStatusCode());

	}
}
