package com.example.urlshortener.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


/**
 * 
 * Represents a log record for each access to a shortcode
 * 
 * @author mlazic
 * 
 */
@Entity
public class AccessLogRecord {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private TinyURL url;

	private int year;

	private int weekNumber;
	

	public AccessLogRecord() {
		super();
	}

	public AccessLogRecord(TinyURL url, int year, int weekNumber) {
		super();
		this.url = url;
		this.year = year;
		this.weekNumber = weekNumber;
	}

	public TinyURL getUrl() {
		return url;
	}

	public void setUrl(TinyURL url) {
		this.url = url;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getWeekNumber() {
		return weekNumber;
	}

	public void setWeekNumber(int weekNumber) {
		this.weekNumber = weekNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

}
