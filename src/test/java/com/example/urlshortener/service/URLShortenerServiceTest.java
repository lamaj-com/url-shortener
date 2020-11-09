package com.example.urlshortener.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.urlshortener.controller.exception.custom.InvalidShortcodeException;
import com.example.urlshortener.controller.exception.custom.InvalidUrlException;
import com.example.urlshortener.controller.exception.custom.ShortcodeNotFoundException;
import com.example.urlshortener.model.RedirectStatsRecord;
import com.example.urlshortener.model.TinyURL;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class URLShortenerServiceTest {

	@Autowired
	private URLShortenerService urlShortenerService;

	@Autowired
	private AccessLogService usageStatsService;

	@Test
	public void testSaveUrl_ok_with_shortcode() {
		TinyURL url = new TinyURL("https://google.com/", "45UFLS");
		TinyURL savedUrl = urlShortenerService.saveURL(url);

		assertEquals("45UFLS", savedUrl.getShortcode());
		assertEquals("https://google.com/", savedUrl.getUrl());
	}

	@Test
	public void testSaveUrl_ok_without_shortcode() {
		TinyURL url = new TinyURL("https://google.com/", "");
		TinyURL savedUrl = urlShortenerService.saveURL(url);

		assertFalse(savedUrl.getShortcode().isEmpty());
		assertTrue(savedUrl.getShortcode().length() == 6);
	}

	@Test(expected = InvalidShortcodeException.class)
	public void testSaveUrl_nok_shortcode_is_already_in_use() {
		TinyURL url = new TinyURL("https://google.com/", "TEST_9");
		urlShortenerService.saveURL(url);

		TinyURL url2 = new TinyURL("https://google.com/", "TEST_9");
		urlShortenerService.saveURL(url2);
	}

	@Test(expected = InvalidUrlException.class)
	public void testSaveUrl_nok_url_is_not_present() {
		TinyURL url = new TinyURL("", "TEST_9");
		TinyURL savedURL = urlShortenerService.saveURL(url);
		assertNull(savedURL);

	}

	@Test
	public void testIsShortcodeValid_ok() {
		assertTrue(urlShortenerService.isShortcodeValid("USF3_F"));
	}

	@Test
	public void testIsShortcodeValid_nok_shortcode_is_not_valid() {
		assertFalse(urlShortenerService.isShortcodeValid("3eljbakljbb?"));
		assertFalse(urlShortenerService.isShortcodeValid("?-#fsd"));
	}

	@Test(expected = InvalidShortcodeException.class)
	public void testSaveUrl_nok_shortcode_is_not_valid() {
		TinyURL url2 = new TinyURL("https://google.com/", "TEST9");
		urlShortenerService.saveURL(url2);
	}

	@Test
	public void testGetByShortcode_ok() throws URISyntaxException {
		TinyURL url = new TinyURL("https://google.com/", "TEST_9");
		urlShortenerService.saveURL(url);
		TinyURL urlGet = urlShortenerService.getURLByShortcode("TEST_9", true);
		assertEquals(url.getShortcode(), urlGet.getShortcode());
		assertEquals(url.getUrl(), urlGet.getUrl());
	}

	@Test(expected = ShortcodeNotFoundException.class)
	public void testGetByShortcode_nok_shortcode_not_found() throws URISyntaxException {
		urlShortenerService.getURLByShortcode("TEST9", false);
	}

	@Test
	public void testGetUsageStats_ok() throws URISyntaxException {
		TinyURL url = new TinyURL("https://google.com/", "TEST_9");
		urlShortenerService.saveURL(url);
		urlShortenerService.getURLByShortcode("TEST_9", false);
		urlShortenerService.getURLByShortcode("TEST_9", false);
		urlShortenerService.getURLByShortcode("TEST_9", false);

		List<RedirectStatsRecord> stats = usageStatsService.getShortcodeUsageStatiscs(2020, 2020, url.getShortcode());
		LocalDate date = LocalDate.now(ZoneId.of("Europe/Berlin"));
		int weekOfYear = date.get(WeekFields.of(Locale.GERMANY).weekOfYear());
		RedirectStatsRecord redirectStats = new RedirectStatsRecord("TEST_9", date.getYear(), weekOfYear, 3l);
		assertEquals(1, stats.size());
		assertTrue(redirectStats.equals(stats.get(0)));

	}

	@Test
	public void testGetUsageStats_nok_shortcode_invalid() throws URISyntaxException {
		usageStatsService.getShortcodeUsageStatiscs(2020, 2020, "ekljfs");
	}
}
