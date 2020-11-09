package com.example.urlshortener.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.urlshortener.model.TinyURL;

@Repository
public interface TinyURLRepository extends JpaRepository<TinyURL, Long>{
	
	/**
	 * Method used to retrieve tiny url bassed on specified shortcode
	 *
	 * @return a {@link TinyURL} 
	 */
	TinyURL getByShortcode(String shortcode);

	/**
	 * Method used to retrieve a single tiny url based on to the given url
	 *
	 * @return a {@link TinyURL} 
	 */
	Optional<TinyURL> findTopByUrl(String url);
	

}
