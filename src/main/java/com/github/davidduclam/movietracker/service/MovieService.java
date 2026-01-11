package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.TmdbClient;
import com.github.davidduclam.movietracker.dto.TmdbMovieDTO;
import com.github.davidduclam.movietracker.model.Movie;
import com.github.davidduclam.movietracker.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final TmdbClient tmdbClient;

    public MovieService(MovieRepository movieRepository, TmdbClient tmdbClient) {
        this.movieRepository = movieRepository;
        this.tmdbClient = tmdbClient;
    }

    /**
     * Save a movie to the database
     * @param movie the object to be saved
     * @return the saved movie
     */
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Optional<Movie> findByTmdbId(Long tmdbId) {
        return movieRepository.findByTmdbId(tmdbId);
    }

    /**
     * Convert a TmdbMovieDTO to a Movie object
     * @param tmdbMovieDTO the DTO to be converted
     * @return the converted movie
     */
    public Movie convertDtoToMovie(TmdbMovieDTO tmdbMovieDTO) {
        Movie movie = new Movie();
        movie.setTmdbId(tmdbMovieDTO.getId());
        movie.setTitle(tmdbMovieDTO.getTitle());
        movie.setReleaseDate(tmdbMovieDTO.getRelease_date());
        movie.setPosterPath(tmdbMovieDTO.getPoster_path());
        movie.setDescription(tmdbMovieDTO.getOverview());
        return movie;
    }

    /**
     * Search the database for the movie, if not create an entry for the movie in the DB
     * @param tmdbId the movie to be searched for
     * @return the retrieved or existing movie
     */
    public Movie getOrCreateMovie(Long tmdbId) {
        Optional<Movie> m = movieRepository.findByTmdbId(tmdbId);

        if (m.isEmpty()) {
            Movie movie = convertDtoToMovie(tmdbClient.fetchMovieDetails(tmdbId));
            return movieRepository.findByTmdbId(tmdbId).orElseGet(() -> movieRepository.save(movie));
        } else {
            return m.orElseThrow(() -> new NoSuchElementException("Movie not found in DB"));
        }
    }

}
