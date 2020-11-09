package com.example.urlshortener.controller.rest.dto;

public class RedirectStatsDTO {
	
	private int year;

	private int weekNumber;

	private long redirect_counts;
	
	

	public RedirectStatsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RedirectStatsDTO(int year, int weekNumber, long redirect_counts) {
		super();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (redirect_counts ^ (redirect_counts >>> 32));
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
		RedirectStatsDTO other = (RedirectStatsDTO) obj;
		if (redirect_counts != other.redirect_counts)
			return false;
		if (weekNumber != other.weekNumber)
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	
}
