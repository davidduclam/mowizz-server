package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.TmdbClient;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbImageResultsDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbMovieDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbVideoDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbVideoResultsDTO;
import com.github.davidduclam.movietracker.dto.MovieResponseDTO;
import com.github.davidduclam.movietracker.dto.TrailerDTO;
import com.github.davidduclam.movietracker.dto.UserMediaRequestDTO;
import com.github.davidduclam.movietracker.error.MediaNotFoundException;
import com.github.davidduclam.movietracker.model.MediaType;
import com.github.davidduclam.movietracker.model.Movie;
import com.github.davidduclam.movietracker.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private TmdbClient tmdbClient;

    @InjectMocks
    private MovieService movieService;

    @Test
    void saveMovieToDb_movieNotInDb_savesMovie() {
        TmdbVideoResultsDTO tmdbVideoResultsDTO = new TmdbVideoResultsDTO(List.of());
        TmdbMovieDTO tmdbMovieDTO = new TmdbMovieDTO(1L,"", LocalDate.now(), "", "", "", 2.0, tmdbVideoResultsDTO, new TmdbImageResultsDTO(List.of()));

        UserMediaRequestDTO userMedia = new UserMediaRequestDTO(1L, MediaType.MOVIE);
        when(movieRepository.findByTmdbId(1L)).thenReturn(Optional.empty());
        when(tmdbClient.fetchMovieDetails(1L)).thenReturn(tmdbMovieDTO);

        movieService.saveMovieToDb(userMedia);

        verify(movieRepository).save(any(Movie.class));
    }

    @Test
    void saveMovieToDb_movieAlreadyInDb_doesNotSave() {
        UserMediaRequestDTO userMedia = new UserMediaRequestDTO(1L, MediaType.MOVIE);
        when(movieRepository.findByTmdbId(1L)).thenReturn(Optional.of(new Movie()));

        movieService.saveMovieToDb(userMedia);

        verify(movieRepository, never()).save(any());
    }

    @Test
    void getMovieFromDb_foundMovie() {
        Movie movie = new Movie();
        movie.setTmdbId(1L);
        movie.setTitle("Inception");

        when(movieRepository.findByTmdbId(1L)).thenReturn(Optional.of(movie));

        MovieResponseDTO result = movieService.getMovieFromDb(1L);

        assertEquals(1L, result.id());
        assertEquals("Inception", result.title());
    }

    @Test
    void getMovieFromDb_notFound_throwsException() {
        assertThrows(MediaNotFoundException.class, () -> movieService.getMovieFromDb(1L));
    }

    @Test
    void testPopularMovies() {
        List<TmdbMovieDTO> list = new ArrayList<>();
        TmdbVideoResultsDTO videos = new TmdbVideoResultsDTO(List.of());
        TmdbMovieDTO movie = new TmdbMovieDTO(1L, "", LocalDate.now(), "", "", "", 0.0, videos, new TmdbImageResultsDTO(List.of()));
        list.add(movie);

        when(tmdbClient.popularMovies()).thenReturn(list);

        List<MovieResponseDTO> result = movieService.popularMovies();

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
    }

    @Test
    void testTopRatedMovies() {
        List<TmdbMovieDTO> list = new ArrayList<>();
        TmdbVideoResultsDTO videos = new TmdbVideoResultsDTO(List.of());
        TmdbMovieDTO movie = new TmdbMovieDTO(1L, "", LocalDate.now(), "", "", "", 0.0, videos, new TmdbImageResultsDTO(List.of()));
        list.add(movie);

        when(tmdbClient.topRatedMovies()).thenReturn(list);

        List<MovieResponseDTO> result = movieService.topRatedMovies();

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
    }

    @Test
    void testUpcomingMovies() {
        List<TmdbMovieDTO> list = new ArrayList<>();
        TmdbVideoResultsDTO videos = new TmdbVideoResultsDTO(List.of());
        TmdbMovieDTO movie = new TmdbMovieDTO(1L, "", LocalDate.now(), "", "", "", 0.0, videos, new TmdbImageResultsDTO(List.of()));
        list.add(movie);

        when(tmdbClient.upcomingMovies()).thenReturn(list);

        List<MovieResponseDTO> result = movieService.upcomingMovies();

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
    }

    @Test
    void getMovieTrailer_foundTrailer() {
        List<TmdbVideoDTO> list = new ArrayList<>();
        TmdbVideoDTO video = new TmdbVideoDTO("Some Movie Trailer", true, "abc123", "YouTube", "Trailer");
        list.add(video);

        when(tmdbClient.fetchMovieTrailers(1L)).thenReturn(list);

        TrailerDTO result = movieService.getMovieTrailer(1L);

        assertEquals("Some Movie Trailer", result.name());
        assertEquals("abc123", result.key());
    }

    @Test
    void getMovieTrailer_throwsException() {
        assertThrows(MediaNotFoundException.class, () -> movieService.getMovieTrailer(1L));
    }
}
