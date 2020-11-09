package com.example.urlshortener.service;

import java.util.List;

import com.example.urlshortener.controller.exception.custom.InvalidShortcodeException;
import com.example.urlshortener.model.RedirectStatsRecord;
import com.example.urlshortener.model.TinyURL;

public interface AccessLogService {

	/**
	 * Retrieves tiny url usage statistics in given period 
	 *
	 * 
	 * @param startYear  beginning of the period for which usage statistics is needed
	 * @param endYear  end of the period for which usage statistics is needed
	 * @param shortcode  for which usage statistics is needed 
	 * @return list of redirect stats records
	 * @throws InvalidShortcodeException
	 */
	List<RedirectStatsRecord> getShortcodeUsageStatiscs(Integer startYear, Integer endYear, String shortcode);

	/**
	 * Saves access log record for the given tiny url
	 *
	 * 
	 * @param url  tiny url
	 * 
	 * @throws InvalidShortcodeException
	 */
	void saveAccessLogRecord(TinyURL url);

}
