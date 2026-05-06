package com.ben.content_distribution_api;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(
	    uniqueConstraints = @UniqueConstraint(columnNames = {"title", "release_year"})
	)
public class Film {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String title;
	
	private String genre;
	
	@Column(nullable = false, name = "release_year")
	@Min(value = 1888, message = "Release year must be between 1888 and current year")
	private int releaseYear;
	
	public Film() {}

    public Film(Long id, String title, String genre, int releaseYear) {
    	this.id = id;
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}
}