package com.ben.content_distribution_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO representing incoming/outgoing Film data.
 * Decouples API contract from persistence model.
 */
public class FilmDTO {

	private Long id;
	
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotNull(message = "Release year is required")
    private Integer releaseYear;

    public FilmDTO() {}

    public FilmDTO(Long id, String title, String genre, Integer releaseYear) {
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

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
}