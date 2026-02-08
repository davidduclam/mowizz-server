package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.TmdbClient;
import com.github.davidduclam.movietracker.dto.TmdbTvShowDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TvShowService {
    private final TmdbClient tmdbClient;

    public TvShowService(TmdbClient tmdbClient) {
        this.tmdbClient = tmdbClient;
    }

    /**
     * Fetches the details of a specific TV show by its TMDB ID.
     *
     * This method interacts with the TMDB client to retrieve detailed information
     * about a TV show, such as its name, description, air date, and ratings.
     *
     * @param tmdbId the unique identifier of the TV show in the TMDB database
     * @return a {@code TmdbTvShowDTO} object containing the details of the requested TV show
     */
    public TmdbTvShowDTO fetchTvShowDetails(Long tmdbId) {
        return tmdbClient.fetchTvShowDetails(tmdbId);
    }

    /**
     * Retrieves a list of popular TV shows from the TMDB API.
     *
     * This method interacts with the TMDB client to fetch the current list
     * of TV shows that are trending or popular based on the API's criteria.
     *
     * @return a list of {@code TmdbTvShowDTO} objects representing the popular TV shows.
     *         The returned list may be empty if no popular TV shows are found.
     */
    public List<TmdbTvShowDTO> popularTvShows() {
        return tmdbClient.popularTvShows();
    }
}
