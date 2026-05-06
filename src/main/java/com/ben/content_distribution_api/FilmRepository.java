package com.ben.content_distribution_api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FilmRepository extends JpaRepository<Film, Long>, 
JpaSpecificationExecutor<Film> {
	
	boolean existsByTitleAndReleaseYear(String title, Integer releaseYear);

    boolean existsByTitleAndReleaseYearAndIdNot(String title, Integer releaseYear, Long id);
}
