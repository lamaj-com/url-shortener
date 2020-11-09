package com.example.urlshortener.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.urlshortener.controller.rest.URLShortenerController;
import com.example.urlshortener.model.AccessLogRecord;
import com.example.urlshortener.model.RedirectStatsRecord;
import com.example.urlshortener.model.TinyURL;
import com.example.urlshortener.repository.AccessLogRepository;

@Service
public class AccessLogServiceImpl implements AccessLogService {

	AccessLogRepository accessLogRepository;

	@Autowired
	public AccessLogServiceImpl(AccessLogRepository accessLogRepository) {
		this.accessLogRepository = accessLogRepository;
	}

	private final static Logger LOGGER = LoggerFactory.getLogger(URLShortenerController.class);

	public List<RedirectStatsRecord> getShortcodeUsageStatiscs(Integer startDate, Integer endDate, String shortcode) {
		LOGGER.debug("GetShortcodeUsageStatistics: '{}'", shortcode);
		return accessLogRepository.getUsageStatisticsGroupByYearAndWeek(endDate, endDate, shortcode);
	}

	public void saveAccessLogRecord(TinyURL url) {
		LOGGER.debug("SaveAccessLogRecord: '{}'", url);
		if (url != null) {
			LocalDate date = LocalDate.now(ZoneId.of("Europe/Berlin"));
			int weekOfYear = date.get(WeekFields.of(Locale.GERMANY).weekOfYear());
			accessLogRepository.save(new AccessLogRecord(url, date.getYear(), weekOfYear));

		}
	}
}
