package com.ben.content_distribution_api;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ben.content_distribution_api.dto.FilmDTO;
import com.ben.content_distribution_api.exception.DuplicateFilmException;

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
}
