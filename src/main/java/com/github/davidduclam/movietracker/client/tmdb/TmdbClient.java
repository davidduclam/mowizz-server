package com.github.davidduclam.movietracker.client.tmdb;

import com.github.davidduclam.movietracker.dto.TmdbMovieDTO;
import com.github.davidduclam.movietracker.dto.TmdbSearchMovieResponseDTO;
import com.github.davidduclam.movietracker.dto.TmdbSearchTvShowResponseDTO;
import com.github.davidduclam.movietracker.dto.TmdbTvShowDTO;
import com.github.davidduclam.movietracker.error.TmdbClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.function.Supplier;

@Service
public class TmdbClient {

    private final RestClient restClient;
    private static final Logger logger = LoggerFactory.getLogger(TmdbClient.class);

    public TmdbClient(RestClient.Builder builder, @Value("${tmdb.access-token}") String accessToken) {
        this.restClient = builder
                .baseUrl("https://api.themoviedb.org/3")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

    // =======================================================
    // Movie endpoints
    // =======================================================

    /**
     * Search a movie from TMDB based on a keyword
     *
     * @param query the keyword to be searched for
     * @return the list of movies
     * @throws TmdbClientException if an error occurs while searching for movies
     */
    public List<TmdbMovieDTO> searchMovies(String query) {
        TmdbSearchMovieResponseDTO response = execute("search movies", () -> restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/movie")
                        .queryParam("query", query)
                        .queryParam("language", "en-US")
                        .build())
                .retrieve()
                .body(TmdbSearchMovieResponseDTO.class));

        if (response == null) {
            throw new TmdbClientException("TMDB returned an empty response for search");
        }
        logger.info("Found {} movies for query {}", response.getTotal_results(), query);
        return response.getResults();
    }

    /**
     * Fetch the details of a specific movie
     *
     * @param tmdbId the ID of the movie from TMDB
     * @return the movie details from the specified movie
     * @throws TmdbClientException if an error occurs while fetching movie details
     */
    public TmdbMovieDTO fetchMovieDetails(Long tmdbId) {
        TmdbMovieDTO response = execute("fetch movie details", () -> restClient.get()
                .uri("/movie/{movie_id}", tmdbId)
                .retrieve()
                .body(TmdbMovieDTO.class));
        if (response == null) {
            throw new TmdbClientException("TMDB returned an empty response for movie details");
        }
        return response;
    }

    /**
     * Get a list of movies ordered by popularity
     *
     * @return the list of popular movies
     * @throws TmdbClientException if an error occurs while fetching popular movies
     */
    public List<TmdbMovieDTO> popularMovies() {
        TmdbSearchMovieResponseDTO response = execute("fetch popular movies", () -> restClient.get()
                .uri("/movie/popular")
                .retrieve()
                .body(TmdbSearchMovieResponseDTO.class));
        if (response == null) {
            throw new TmdbClientException("TMDB returned an empty response for popular movies");
        }
        return response.getResults();
    }

    /**
     * Get a list of movies ordered by rating
     *
     * @return the list of top-rated movies
     * @throws TmdbClientException if an error occurs while fetching top-rated movies
     */
    public List<TmdbMovieDTO> topRatedMovies() {
        TmdbSearchMovieResponseDTO response = execute("fetch top-rated movies", () -> restClient.get()
                .uri("/movie/top_rated")
                .retrieve()
                .body(TmdbSearchMovieResponseDTO.class));
        if (response == null) {
            throw new TmdbClientException("TMDB returned an empty response for top-rated movies");
        }
        return response.getResults();
    }

    /**
     * Get a list of movies that are being released soon
     *
     * @return the list of upcoming movies
     * @throws TmdbClientException if an error occurs while fetching upcoming movies
     */
    public List<TmdbMovieDTO> upcomingMovies() {
        TmdbSearchMovieResponseDTO response = execute("fetch upcoming movies", () -> restClient.get()
                .uri("/movie/upcoming")
                .retrieve()
                .body(TmdbSearchMovieResponseDTO.class));
        if (response == null) {
            throw new TmdbClientException("TMDB returned an empty response for upcoming movies");
        }
        return response.getResults();
    }

    // =======================================================
    // TV Show endpoints
    // =======================================================

    /**
     * Fetches the details of a specific TV show from TMDB.
     *
     * @param tmdbId the ID of the TV show from TMDB
     * @return the details of the specified TV show as a {@code TmdbTvShowDTO} object
     * @throws TmdbClientException if an error occurs while fetching TV show details
     */
    public TmdbTvShowDTO fetchTvShowDetails(Long tmdbId) {
        TmdbTvShowDTO response = execute("fetch tv show details", () -> restClient.get()
                .uri("/tv/{series_id}", tmdbId)
                .retrieve()
                .body(TmdbTvShowDTO.class));
        if (response == null) {
            throw new TmdbClientException("TMDB returned an empty response for tv show details");
        }
        return response;
    }

    /**
     * Fetches a list of popular TV shows from the TMDB API.
     *
     * This method makes a request to the TMDB API's "popular TV shows" endpoint
     * and processes the response to extract and return the list of popular TV shows.
     * If the response is null, an exception is thrown to indicate an error retrieving data.
     *
     * @return a list of {@code TmdbMovieDTO} objects representing popular TV shows
     *         retrieved from the TMDB API. The list may be empty if no shows are found.
     * @throws TmdbClientException if the TMDB API returns an empty response
     */
    public List<TmdbTvShowDTO> popularTvShows() {
        TmdbSearchTvShowResponseDTO response = execute("fetch popular tv shows", () -> restClient.get()
                .uri("/tv/popular")
                .retrieve()
                .body(TmdbSearchTvShowResponseDTO.class));
        if (response == null) {
            throw new TmdbClientException("TMDB returned an empty response for popular tv shows");
        }
        return response.getResults();
    }

    /**
     * Fetches a list of top-rated TV shows from the TMDB API.
     *
     * This method sends a request to the TMDB API to retrieve the top-rated TV shows and returns
     * a list of {@code TmdbTvShowDTO} objects that represent the results. If the response from
     * the API is null, an exception is thrown.
     *
     * @return a list of {@code TmdbTvShowDTO} objects containing information about the top-rated TV shows
     * @throws TmdbClientException if the API response is null or cannot be retrieved correctly
     */
    public List<TmdbTvShowDTO> topRatedTvShows() {
        TmdbSearchTvShowResponseDTO response = execute("fetch top rated tv shows", () -> restClient.get()
                .uri("/tv/top_rated")
                .retrieve()
                .body(TmdbSearchTvShowResponseDTO.class));
        if (response == null) {
            throw new TmdbClientException("TMDB returned an empty response for top rated tv shows");
        }
        return response.getResults();
    }


    // =======================================================
    // Internal helper
    // =======================================================

    /**
     * Executes a given action by invoking the provided Supplier and handles any exceptions
     * related to the TMDB API during the execution.
     *
     * @param <T> the type of the result produced by the supplier
     * @param action a string describing the action being executed (used for logging and error messages)
     * @param call the supplier representing the action to be executed
     * @return the result of the action executed by the supplier
     * @throws TmdbClientException if a TMDB-specific error or any other RestClient exception occurs
     */
    private <T> T execute(String action, Supplier<T> call) {
        try {
            return call.get();
        } catch (RestClientResponseException e) {
            String body = e.getResponseBodyAsString();
            String trimmedBody = body == null ? "" : body.substring(0, Math.min(body.length(), 200));
            logger.error("TMDB {} failed: status={} body={}", action, e.getStatusCode(), trimmedBody, e);
            throw new TmdbClientException(
                    "TMDB " + action + " failed with status " + e.getStatusCode().value(),
                    e.getStatusCode(),
                    e
            );
        } catch (RestClientException e) {
            logger.error("TMDB {} failed: {}", action, e.getMessage(), e);
            throw new TmdbClientException("TMDB " + action + " failed", e);
        }
    }
}
