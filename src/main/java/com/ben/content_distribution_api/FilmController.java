package com.ben.content_distribution_api;

import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ben.content_distribution_api.dto.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * REST controller for film resource endpoints.
 *
 * Exposes CRUD operations along with filtering,
 * sorting, and pagination capabilities.
 */
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

	private final FilmService filmService;
	
	public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }
	
	@Operation(
	        summary = "Get films",
	        description = "Retrieves films with optional filtering, sorting, and pagination."
	)
	@GetMapping
	public ResponseEntity<PagedResponse<FilmDTO>> getAllFilms(
			@RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be 0 or greater") int page,
			@RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be at least 1") 
			@Max(value = 100, message = "Size must be 100 or less") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String direction,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String genre,
			@RequestParam(required = false) Integer releaseYear) {
	
		PagedResponse<FilmDTO> films = filmService.getAllFilms(
				page, 
				size, 
				sortBy, 
				direction,
				title,
				genre,
				releaseYear);
		return ResponseEntity.ok(films);
	}
	
	@Operation(
			summary = "Create film",
			description = "Creates a new film entry with provided details."
	)
	@PostMapping
	public ResponseEntity<FilmDTO> createFilm(@Valid @RequestBody FilmDTO dto) {
		FilmDTO created = filmService.createFilm(dto);
	    return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}
	
	
	@Operation(
            summary = "Get film by ID",
            description = "Retrieves a specific film entry by its unique ID."
    )
	@GetMapping("/{id}")
	public ResponseEntity<FilmDTO> getFilmById(@PathVariable Long id) {
	    FilmDTO film = filmService.getFilmById(id);
	    return ResponseEntity.ok(film);
	}
	
	@Operation(
            summary = "Replace film",
            description = "Fully replaces a specific film entry by its unique ID."
    )
	@PutMapping("/{id}")
	public ResponseEntity<FilmDTO> updateFilm(
	        @PathVariable Long id,
	        @Valid @RequestBody FilmDTO dto) {

	    FilmDTO updatedFilm = filmService.updateFilm(id, dto);
	    return ResponseEntity.ok(updatedFilm);
	}
	
	@Operation(
            summary = "Patch film",
            description = "Updates a specific film entry by its unique ID with provided details. Only provided fields will be changed."
    )
	@PatchMapping("/{id}")
	public ResponseEntity<FilmDTO> patchFilm(
			@PathVariable Long id,
            @Valid @RequestBody FilmPatchDTO dto) {
		
		FilmDTO updatedFilm = filmService.patchFilm(id, dto);
		return ResponseEntity.ok(updatedFilm);
	}
	
	@Operation(
			summary = "Delete film",
			description = "Permanently deletes a specific film entry by its unique ID. Deleted IDs are not reused or renumbered."
	)	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFilm(@PathVariable Long id) {
	    filmService.deleteFilm(id);
	    return ResponseEntity.noContent().build();
	}
}
