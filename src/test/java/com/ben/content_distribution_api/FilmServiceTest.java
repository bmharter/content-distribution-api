package com.ben.content_distribution_api;

import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ben.content_distribution_api.dto.*;
import com.ben.content_distribution_api.exception.DuplicateFilmException;
import com.ben.content_distribution_api.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class FilmServiceTest {

	@Mock
	private FilmRepository filmRepository;
	
	@InjectMocks
	private FilmService filmService;
	
	@Test
    void createFilm_whenFilmDoesNotExist_savesAndReturnsFilmDTO() {
        FilmDTO input = new FilmDTO(null, "Alien", "Sci-Fi Horror", 1979);
        Film savedFilm = new Film(1L, "Alien", "Sci-Fi Horror", 1979);

        when(filmRepository.existsByTitleAndReleaseYear("Alien", 1979))
            .thenReturn(false);

        when(filmRepository.save(any(Film.class)))
            .thenReturn(savedFilm);

        FilmDTO result = filmService.createFilm(input);

        assertEquals(1L, result.getId());
        assertEquals("Alien", result.getTitle());
        assertEquals("Sci-Fi Horror", result.getGenre());
        assertEquals(1979, result.getReleaseYear());

        verify(filmRepository).existsByTitleAndReleaseYear("Alien", 1979);
        verify(filmRepository).save(any(Film.class));
    }

    @Test
    void createFilm_whenDuplicateExists_throwsDuplicateFilmException() {
        FilmDTO input = new FilmDTO(null, "Alien", "Sci-Fi Horror", 1979);

        when(filmRepository.existsByTitleAndReleaseYear("Alien", 1979))
            .thenReturn(true);

        assertThrows(DuplicateFilmException.class, () -> filmService.createFilm(input));

        verify(filmRepository).existsByTitleAndReleaseYear("Alien", 1979);
        verify(filmRepository, never()).save(any(Film.class));
    }
    
    @Test
    void getFilmById_whenFilmExists_returnsFilmDTO() {
        Film savedFilm = new Film(1L, "Alien", "Sci-Fi Horror", 1979);
        
        when(filmRepository.findById(1L))
           .thenReturn(Optional.of(savedFilm));
        
        FilmDTO result = filmService.getFilmById(1L);
        
        assertEquals(1L, result.getId());
        assertEquals("Alien", result.getTitle());
        assertEquals("Sci-Fi Horror", result.getGenre());
        assertEquals(1979, result.getReleaseYear());
        
        verify(filmRepository).findById(1L);
    }
    
    @Test
    void getFilmById_whenFilmDoesNotExist_throwsResourceNotFoundException() {
    	when(filmRepository.findById(1L))
           .thenReturn(Optional.empty());
        
    	ResourceNotFoundException ex = 
    		assertThrows(ResourceNotFoundException.class, () -> filmService.getFilmById(1L));
        
    	assertEquals("Film not found with id: 1", ex.getMessage());
    	
        verify(filmRepository).findById(1L);;
    }
    
    @Test
    void deleteFilm_whenFilmExists_deletesFilm() {
    	Film savedFilm = new Film(1L, "Alien", "Sci-Fi Horror", 1979);
        
        when(filmRepository.findById(1L))
           .thenReturn(Optional.of(savedFilm));
        
        filmService.deleteFilm(1L);
        
        verify(filmRepository).findById(1L);
        verify(filmRepository).delete(savedFilm);
    }
    
    @Test
    void deleteFilm_whenFilmDoesNotExist_throwsResourceNotFoundException() {
    	when(filmRepository.findById(1L))
           .thenReturn(Optional.empty());
        
        ResourceNotFoundException ex = 
            assertThrows(ResourceNotFoundException.class, () -> filmService.deleteFilm(1L));
        
        assertEquals("Film not found with id: 1", ex.getMessage());
        
        verify(filmRepository).findById(1L);
        verify(filmRepository, never()).delete(any(Film.class));;
    }
    
    @Test
    void updateFilm_whenFilmExists_updatesAndReturnsFilmDTO() {
    	Film existingFilm = new Film(1L, "Alien", "Sci-Fi Horror", 1979);
        Film updatedFilm = new Film(1L, "Alien: Covenant", "Sci-Fi", 2018);
        FilmDTO input = new FilmDTO(1L, "Alien: Covenant", "Sci-Fi", 2018);
        
        when(filmRepository.findById(1L))
           .thenReturn(Optional.of(existingFilm));
        
        when(filmRepository.existsByTitleAndReleaseYearAndIdNot(input.getTitle(), input.getReleaseYear(), 1L))
           .thenReturn(false);
        
        when(filmRepository.save(any(Film.class)))
           .thenReturn(updatedFilm);
        
        FilmDTO result = filmService.updateFilm(1L, input);
        
        ArgumentCaptor<Film> filmCaptor = ArgumentCaptor.forClass(Film.class);
        
        verify(filmRepository).save(filmCaptor.capture());
        
        Film saved = filmCaptor.getValue();
        
        assertEquals(1L, result.getId());
        assertEquals("Alien: Covenant", result.getTitle());
        assertEquals("Sci-Fi", result.getGenre());
        assertEquals(2018, result.getReleaseYear());
        
        assertEquals(1L, saved.getId());
        assertEquals("Alien: Covenant", saved.getTitle());
        assertEquals("Sci-Fi", saved.getGenre());
        assertEquals(2018, saved.getReleaseYear());
        
        verify(filmRepository).findById(1L);
        verify(filmRepository).existsByTitleAndReleaseYearAndIdNot(input.getTitle(), input.getReleaseYear(), 1L);
        verify(filmRepository).save(any(Film.class));;
    }
    
    @Test
    void updateFilm_whenFilmDoesNotExist_throwsResourceNotFoundException() {
    	when(filmRepository.findById(1L))
           .thenReturn(Optional.empty());
        
        ResourceNotFoundException ex = 
            assertThrows(ResourceNotFoundException.class, () 
        		-> filmService.updateFilm(1L, new FilmDTO(1L, "Alien", "Sci-Fi Horror", 1979)));
        
        assertEquals("Film not found with id: 1", ex.getMessage());
        
        verify(filmRepository).findById(1L);
        verify(filmRepository, never()).save(any(Film.class));;
    }
    
    @Test
    void updateFilm_whenDuplicateExists_throwsDuplicateFilmException() {
    	Film existingFilm = new Film(1L, "Alien", "Sci-Fi Horror", 1979);
        FilmDTO input = new FilmDTO(1L, "Alien", "Sci-Fi", 1979);
        
        when(filmRepository.findById(1L))
           .thenReturn(Optional.of(existingFilm));
        
        when(filmRepository.existsByTitleAndReleaseYearAndIdNot(input.getTitle(), input.getReleaseYear(), 1L))
           .thenReturn(true);
        
        assertThrows(DuplicateFilmException.class, () -> filmService.updateFilm(1L, input));
        
        verify(filmRepository).findById(1L);
        verify(filmRepository).existsByTitleAndReleaseYearAndIdNot(input.getTitle(), input.getReleaseYear(), 1L);
        verify(filmRepository, never()).save(any(Film.class));;
    }
    
    @Test
    void patchFilm_singleField_updatesAndReturnsFilmDTO() {
    	Film existingFilm = new Film(1L, "Alien", "Sci-Fi Horror", 1979);
    	Film patchedFilm = new Film(1L, "Alien", "Sci-Fi", 1979);
    	
    	FilmPatchDTO input = new FilmPatchDTO();
    	input.setGenre("Sci-Fi");
    	
    	when(filmRepository.findById(1L))
    		.thenReturn(Optional.of(existingFilm));
    	
    	when(filmRepository.save(any(Film.class)))
    		.thenReturn(patchedFilm);	
    	
    	FilmDTO result = filmService.patchFilm(1L, input);
    	
    	ArgumentCaptor<Film> filmCaptor = ArgumentCaptor.forClass(Film.class);
    	verify(filmRepository).save(filmCaptor.capture());
    	
    	Film filmToSave = filmCaptor.getValue();
    	
    	assertEquals(1L, filmToSave.getId());
        assertEquals("Alien", filmToSave.getTitle());
        assertEquals("Sci-Fi", filmToSave.getGenre());
        assertEquals(1979, filmToSave.getReleaseYear());

        assertEquals(1L, result.getId());
        assertEquals("Alien", result.getTitle());
        assertEquals("Sci-Fi", result.getGenre());
        assertEquals(1979, result.getReleaseYear());

        verify(filmRepository).findById(1L);
    }
    
    @Test
    void patchFilm_whenMergedTitleAndReleaseYearWouldDuplicate_throwsDuplicateFilmException() {
    	Film duplicate = new Film(2L, "The Thing", "Horror", 1982);
    	
    	FilmPatchDTO input = new FilmPatchDTO();
    	
    	input.setTitle("Alien");
    	input.setReleaseYear(1979);
    	
    	when(filmRepository.findById(2L))
    		.thenReturn(Optional.of(duplicate));
    	
    	when(filmRepository.existsByTitleAndReleaseYearAndIdNot("Alien", 1979, 2L))
    		.thenReturn(true);
    	
    	assertThrows(DuplicateFilmException.class, () -> filmService.patchFilm(2L, input));
    	
    	verify(filmRepository).findById(2L);
    	verify(filmRepository).existsByTitleAndReleaseYearAndIdNot("Alien", 1979, 2L);
    	verify(filmRepository, never()).save(any(Film.class));
    }
    
    @Test
    void patchFilm_whenPatchedTitleAndExistingReleaseYearWouldDuplicate_throwsDuplicateFilmException() {
    	Film existingFilm = new Film(2L, "The Thing", "Horror", 1979);

        FilmPatchDTO input = new FilmPatchDTO();
        input.setTitle("Alien");

        when(filmRepository.findById(2L))
            .thenReturn(Optional.of(existingFilm));

        when(filmRepository.existsByTitleAndReleaseYearAndIdNot("Alien", 1979, 2L))
            .thenReturn(true);

        assertThrows(DuplicateFilmException.class,
            () -> filmService.patchFilm(2L, input));

        verify(filmRepository).findById(2L);
        verify(filmRepository).existsByTitleAndReleaseYearAndIdNot("Alien", 1979, 2L);
        verify(filmRepository, never()).save(any(Film.class));
    }
}
