package com.example.urlshortener.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.urlshortener.model.AccessLogRecord;
import com.example.urlshortener.model.RedirectStatsRecord;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLogRecord, Long> {

	/**
	 * Method used to retrieve usage statistics of the getShortcode service
	 * for the specified shortcode in specified time span
	 *
	 * @return a {@link RedirectStatsRecord} 
	 */
	@Query("SELECT new com.example.urlshortener.model.RedirectStatsRecord(r.url.shortcode as shortcode, r.year as year, r.weekNumber as weekNumber, count(r) as redirects_count) "
			+ "FROM com.example.urlshortener.model.AccessLogRecord r " + "GROUP BY shortcode, weekNumber,year HAVING shortcode LIKE :shortcode AND year BETWEEN :startYear AND :endYear")
	List<RedirectStatsRecord> getUsageStatisticsGroupByYearAndWeek(@Param("startYear") int startYear,
			@Param("endYear") int endYear, @Param("shortcode") String shortcode);

}
