package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.TmdbClient;
import com.github.davidduclam.movietracker.dto.TmdbMovieDTO;
import com.github.davidduclam.movietracker.error.MediaAlreadyExistsException;
import com.github.davidduclam.movietracker.model.Movie;
import com.github.davidduclam.movietracker.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.davidduclam.movietracker.service.UserMediaService.getMovie;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final TmdbClient tmdbClient;

    public MovieService(MovieRepository movieRepository, TmdbClient tmdbClient) {
        this.movieRepository = movieRepository;
        this.tmdbClient = tmdbClient;
    }

    /**
     * Converts a TmdbMovieDTO object to a Movie entity.
     *
     * @param tmdbMovieDTO the {@code TmdbMovieDTO} object containing movie details retrieved from TMDb
     * @return a {@code Movie} entity populated with data from the provided {@code TmdbMovieDTO}
     */
    private Movie convertTmdbMovieDtoToMovie(TmdbMovieDTO tmdbMovieDTO) {
        return getMovie(tmdbMovieDTO);
    }

    /**
     * Saves a movie to the database by its TMDb ID. If the movie with the given TMDb ID
     * already exists in the database, a {@code MediaAlreadyExistsException} is thrown.
     * This method fetches movie details from TMDb, converts the data into a {@code Movie}
     * entity, and then persists it to the database.
     *
     * @param tmdbId the unique ID of the movie from TMDb
     * @return the saved {@code Movie} entity
     * @throws MediaAlreadyExistsException if a movie with the provided TMDb ID already exists in the database
     */
    public Movie saveMovieToDb(Long tmdbId) {
        if (movieRepository.findByTmdbId(tmdbId).isPresent()) {
            throw new MediaAlreadyExistsException("Movie already added");
        }

        Movie movie = convertTmdbMovieDtoToMovie(fetchMovieDetails(tmdbId));
        return movieRepository.save(movie);
    }

    /**
     * Uses TmdbClient to search for a movie
     * @param query the keyword
     * @return the list of movies
     */
    public List<TmdbMovieDTO> searchMovie(String query) {
        return tmdbClient.searchMovies(query);
    }

    /**
     * Uses TmdbClient to fetch movie details
     * @param tmdbId the movie id
     * @return the movie details
     */
    public TmdbMovieDTO fetchMovieDetails(Long tmdbId) {
        return tmdbClient.fetchMovieDetails(tmdbId);
    }

    /**
     * Uses TmdbClient to fetch the most popular movies
     * @return the list of popular movies
     */
    public List<TmdbMovieDTO> popularMovies() {
        return tmdbClient.popularMovies();
    }

    /**
     * Uses TmdbClient to fetch the top-rated movies
     * @return the list of top-rated movies
     */
    public List<TmdbMovieDTO> topRatedMovies() {
        return tmdbClient.topRatedMovies();
    }

    /**
     * Uses TmdbClient to fetch upcoming movies
     * @return the list of upcoming movies
     */
    public List<TmdbMovieDTO> upcomingMovies() {
        return tmdbClient.upcomingMovies();
    }

}
