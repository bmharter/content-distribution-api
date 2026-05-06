package com.ben.content_distribution_api;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import com.ben.content_distribution_api.dto.*;
import com.ben.content_distribution_api.exception.*;
import com.ben.content_distribution_api.specification.FilmSpecification;

@Service
public class FilmService {

	private static final List<String> ALLOWED_SORT_FIELDS =
	        Arrays.asList("id", "title", "genre", "releaseYear");

	private final FilmRepository filmRepository;
	
	public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }
	
	/**
	 * Retrieves films with optional filtering, sorting, and pagination.
	 *
	 * Filters are combined using AND logic:
	 * - title: case-insensitive substring match
	 * - genre: case-insensitive exact match
	 * - releaseYear: exact match
	 *
	 * If no filters are provided, returns all films paginated.
	 */
	public PagedResponse<FilmDTO> getAllFilms(
			int page, 
			int size, 
			String sortBy, 
			String direction,
			String title,
			String genre,
			Integer releaseYear) {
		
		if(!isAllowedSortField(sortBy)) {
			throw new InvalidSortFieldException("Invalid sort field: " + sortBy);
		}
		
		// Ensures and implements a valid sort direction.
		if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
		    throw new InvalidSortFieldException("Invalid sort direction: " + direction);
		}
		
		Sort sort = direction.equalsIgnoreCase("desc")
				? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		
		
		// Create a Pageable to fill in with filtered results.
		Pageable pageable = PageRequest.of(page, size, sort);
		
		// Build the specification filter.
		Specification<Film> spec = null;
		
		if(title != null && !title.isBlank()) {
			spec = Specification.where(FilmSpecification.titleContains(title));
		}
		
		if (genre != null && !genre.isBlank()) {
			spec = (spec == null)
			        ? FilmSpecification.genreEquals(genre)
			        : spec.and(FilmSpecification.genreEquals(genre));
	    }

	    if (releaseYear != null) {
	    	spec = (spec == null)
	    			? FilmSpecification.releaseYearEquals(releaseYear)
                    : spec.and(FilmSpecification.releaseYearEquals(releaseYear));
	    }
		
		Page<Film> filmPage = (spec == null
		        ? filmRepository.findAll(pageable)
		        : filmRepository.findAll(spec, pageable));

		        List<FilmDTO> dtos = filmPage
		                .map(this::toDTO)
		                .getContent();

		        return new PagedResponse<>(
		                dtos,
		                filmPage.getNumber(),
		                filmPage.getSize(),
		                filmPage.getTotalElements(),
		                filmPage.getTotalPages()
		        );
	}
	
	public FilmDTO createFilm(FilmDTO dto) {
		if (filmRepository.existsByTitleAndReleaseYear(dto.getTitle(), dto.getReleaseYear())) {
		    throw new DuplicateFilmException("A film with that title and release year already exists");
		}
		
	    Film film = toEntity(dto);
	    Film saved = filmRepository.save(film);
	    return toDTO(saved);
	}
	
	public FilmDTO getFilmById(Long id) {
	    Film film = filmRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));

	    return toDTO(film);
	}
    
    public FilmDTO updateFilm(Long id, FilmDTO dto) {
        Film existingFilm = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
        
        if (filmRepository.existsByTitleAndReleaseYearAndIdNot(dto.getTitle(), dto.getReleaseYear(), id)) {
    	    throw new DuplicateFilmException("A film with that title and release year already exists");
    	}

        existingFilm.setTitle(dto.getTitle());
        existingFilm.setGenre(dto.getGenre());
        existingFilm.setReleaseYear(dto.getReleaseYear());

        Film updatedFilm = filmRepository.save(existingFilm);
        return toDTO(updatedFilm);
    }
    
    /**
     * Applies partial updates to an existing film.
     *
     * The final intended title/releaseYear combination is computed before persistence
     * so uniqueness can be validated against the merged state.
     *
     * @param id the ID of the film to update
     * @param dto optional fields to apply
     * @return updated film data
     */
    public FilmDTO patchFilm(Long id, FilmPatchDTO dto) {
    	// Throw exception if there is no entry with the given id.
    	Film existingFilm = filmRepository.findById(id)
    			.orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
    	
    	// Set the new values if they are provided in the request body.
    	String newTitle = dto.getTitle() != null
    			? dto.getTitle() 
    			: existingFilm.getTitle();
    	Integer newReleaseYear = dto.getReleaseYear()!= null
    			? dto.getReleaseYear() 
    			: existingFilm.getReleaseYear();
    	
    	// Throw exception if an entry with the same title and release year already exists.
    	if (filmRepository.existsByTitleAndReleaseYearAndIdNot(newTitle, newReleaseYear, id)) {
            throw new DuplicateFilmException("A film with that title and release year already exists");
        }
    	
    	// Update the film's values, including genre if provided.
    	existingFilm.setTitle(newTitle);
    	existingFilm.setReleaseYear(newReleaseYear);
    	
    	if(dto.getGenre()!= null) {
    		existingFilm.setGenre(dto.getGenre());
    	}
    	
    	// Save the updated film and return the DTO.
    	Film updatedFilm = filmRepository.save(existingFilm);
    	
    	return toDTO(updatedFilm);
    }
    
    public void deleteFilm(Long id) {
        Film existingFilm = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));

        filmRepository.delete(existingFilm);
    }
    
    /**
     * Converts Film entity to DTO.
     */
    private FilmDTO toDTO(Film film) {
        return new FilmDTO(
        	film.getId(),
            film.getTitle(),
            film.getGenre(),
            film.getReleaseYear()
        );
    }

    /**
     * Converts DTO to Film entity.
     */
    private Film toEntity(FilmDTO dto) {
        Film film = new Film();
        film.setTitle(dto.getTitle());
        film.setGenre(dto.getGenre());
        film.setReleaseYear(dto.getReleaseYear());
        return film;
    }
    
    private boolean isAllowedSortField(String sortBy) {
    	return ALLOWED_SORT_FIELDS.contains(sortBy);
    }
}
