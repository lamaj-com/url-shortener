package com.example.urlshortener.model;

/**
 * 
 * Represents access count for a shortcode per week/year
 * 
 * @author mlazic
 * 
 */
public class RedirectStatsRecord {
	
	private String shortcode;

	private int year;

	private int weekNumber;

	private long redirect_counts;
		

	public RedirectStatsRecord() {
		super();
	}

	public RedirectStatsRecord(String shortcode, int year, int weekNumber, long redirect_counts) {
		super();
		this.shortcode = shortcode;
		this.year = year;
		this.weekNumber = weekNumber;
		this.redirect_counts = redirect_counts;
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

	public long getRedirect_counts() {
		return redirect_counts;
	}

	public void setRedirect_counts(long redirect_counts) {
		this.redirect_counts = redirect_counts;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (redirect_counts ^ (redirect_counts >>> 32));
		result = prime * result + ((shortcode == null) ? 0 : shortcode.hashCode());
		result = prime * result + weekNumber;
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RedirectStatsRecord other = (RedirectStatsRecord) obj;
		if (redirect_counts != other.redirect_counts)
			return false;
		if (shortcode == null) {
			if (other.shortcode != null)
				return false;
		} else if (!shortcode.equals(other.shortcode))
			return false;
		if (weekNumber != other.weekNumber)
			return false;
		if (year != other.year)
			return false;
		return true;
	}
	
	

}
