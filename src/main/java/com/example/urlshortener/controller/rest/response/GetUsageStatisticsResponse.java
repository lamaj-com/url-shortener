package com.example.urlshortener.controller.rest.response;

import java.util.List;

import com.example.urlshortener.controller.rest.dto.RedirectStatsDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * This class represents a response from GetUsageStatistics service
 * 
 * @author mlazic
 * 
 */
public class GetUsageStatisticsResponse {

	
	List<RedirectStatsDTO> result;

	
	@JsonValue
	public List<RedirectStatsDTO> getResult() {
		return result;
	}

	public void setResult(List<RedirectStatsDTO> result) {
		this.result = result;
	}

	@JsonCreator
	public GetUsageStatisticsResponse(List<RedirectStatsDTO> result) {
		super();
		this.result = result;
	}

//	public GetUsageStatisticsResponse() {
//		super();
//	}

}
