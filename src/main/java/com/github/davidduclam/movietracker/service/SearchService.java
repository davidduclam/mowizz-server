package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.TmdbClient;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbSearchResultDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    private final TmdbClient tmdbClient;

    public SearchService(TmdbClient tmdbClient) {
        this.tmdbClient = tmdbClient;
    }

    /**
     * Searches for multiple types of media (movies, TV shows) using the provided query.
     *
     * @param query The search query string to look for matching media items.
     * @return A list of {@code TmdbSearchResultDTO} objects representing the search results.
     */
    public List<TmdbSearchResultDTO> searchMulti(String query) {
        return tmdbClient.searchMulti(query);
    }
}