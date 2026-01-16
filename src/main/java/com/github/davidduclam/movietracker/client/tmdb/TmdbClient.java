package com.github.davidduclam.movietracker.client.tmdb;

import com.github.davidduclam.movietracker.dto.TmdbMovieDTO;
import com.github.davidduclam.movietracker.dto.TmdbSearchResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class TmdbClient {

    private final RestClient restClient;

    public TmdbClient(RestClient.Builder builder, @Value("${tmdb.access-token}") String accessToken) {
        this.restClient = builder
                .baseUrl("https://api.themoviedb.org/3")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

    /**
     * Search a movie from TMDB based on a keyword
     * @param query the keyword to be searched for
     * @return the list of movies
     */
    public List<TmdbMovieDTO> searchMovies(String query) {
        TmdbSearchResponseDTO response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/movie")
                        .queryParam("query", query)
                        .queryParam("language", "en-US")
                        .build())
                .retrieve()
                .body(TmdbSearchResponseDTO.class);
        return response != null ? response.getResults() : List.of();
    }

    /**
     * Fetch the details of a specific movie
     * @param tmdbId the ID of the movie from TMDB
     * @return the movie details from the specified mpvie
     */
    public TmdbMovieDTO fetchMovieDetails(Long tmdbId) {
        return restClient.get()
                .uri("/movie/{movie_id}", tmdbId)
                .retrieve()
                .body(TmdbMovieDTO.class);
    }

    /**
     * Fetch the most popular movies currently
     * @return the list of popular movies
     */
    public List<TmdbMovieDTO> popularMovies() {
        TmdbSearchResponseDTO response = restClient.get()
                .uri("/movie/popular")
                .retrieve()
                .body(TmdbSearchResponseDTO.class);
        return response != null ? response.getResults() : List.of();
    }
}
