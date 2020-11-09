package com.example.urlshortener.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * Configures ModelMapper bean used for conversion between objects
 * 
 * 
 * @author mlazic
 * @since 1.0
 * 
 */
@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
