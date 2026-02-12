package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.TmdbClient;
import com.github.davidduclam.movietracker.dto.AddUserMediaRequestDTO;
import com.github.davidduclam.movietracker.dto.TmdbMovieDTO;
import com.github.davidduclam.movietracker.model.Movie;
import com.github.davidduclam.movietracker.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * @param tmdbMovieDTO the DTO containing movie details from TMDb
     * @return the converted Movie entity
     */
    private Movie convertTmdbMovieDtoToMovie(TmdbMovieDTO tmdbMovieDTO) {
        Movie movie = new Movie();
        movie.setTmdbId(tmdbMovieDTO.getId());
        movie.setTitle(tmdbMovieDTO.getTitle());
        movie.setReleaseDate(tmdbMovieDTO.getRelease_date());
        movie.setPosterPath(tmdbMovieDTO.getPoster_path());
        movie.setBackdropPath(tmdbMovieDTO.getBackdrop_path());
        movie.setOverview(tmdbMovieDTO.getOverview());
        return movie;
    }

    /**
     * Saves a movie to the database if it does not already exist.
     * The movie details are fetched using the TMDb ID provided in the request DTO.
     * The fetched details are converted into a Movie entity and then saved to the repository.
     *
     * @param addUserMediaRequestDTO the DTO containing the TMDb ID of the movie to be saved
     */
    public void saveMovieToDb(AddUserMediaRequestDTO addUserMediaRequestDTO) {
        if (movieRepository.findByTmdbId(addUserMediaRequestDTO.getTmdbId()).isEmpty()) {
            Movie movie = convertTmdbMovieDtoToMovie(fetchMovieDetails(addUserMediaRequestDTO.getTmdbId()));
            movieRepository.save(movie);
        }
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
