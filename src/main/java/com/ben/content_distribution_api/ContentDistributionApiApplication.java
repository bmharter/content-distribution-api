package com.ben.content_distribution_api;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class ContentDistributionApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContentDistributionApiApplication.class, args);
	}

	@Bean
	@Profile("dev")
	CommandLineRunner init(FilmRepository repo) {
	    return args -> {
	    	if(!repo.existsByTitleAndReleaseYear("Alien", 1979)) {
	    		repo.save(new Film(null, "Alien", "Sci-Fi", 1979));
	    	}
	        if(!repo.existsByTitleAndReleaseYear("The Thing", 1982)) {
	        	repo.save(new Film(null, "The Thing", "Horror", 1982));
	        }
	    };
	}
}