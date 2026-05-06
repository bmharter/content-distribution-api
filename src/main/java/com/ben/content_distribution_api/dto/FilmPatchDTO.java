package com.ben.content_distribution_api.dto;

import jakarta.validation.constraints.*;

public class FilmPatchDTO {

	@Pattern(regexp = ".*\\S.*", message = "Title cannot be blank")
    private String title;
	
	@Pattern(regexp = ".*\\S.*", message = "Genre cannot be blank")
    private String genre;
	
	@Min(value = 1888, message = "Release year should be after 1888")
    private Integer releaseYear;

    public FilmPatchDTO() {}

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