package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.TmdbClient;
import com.github.davidduclam.movietracker.dto.TmdbTvShowDTO;
import com.github.davidduclam.movietracker.error.MediaAlreadyExistsException;
import com.github.davidduclam.movietracker.model.TvShow;
import com.github.davidduclam.movietracker.repository.TvShowRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.davidduclam.movietracker.service.UserMediaService.getTvShow;

@Service
public class TvShowService {
    private final TvShowRepository tvShowRepository;
    private final TmdbClient tmdbClient;

    public TvShowService(TvShowRepository tvShowRepository, TmdbClient tmdbClient) {
        this.tvShowRepository = tvShowRepository;
        this.tmdbClient = tmdbClient;
    }

    /**
     * Converts a {@code TmdbTvShowDTO} object into a {@code TvShow} object.
     *
     * This method maps the properties of the given {@code TmdbTvShowDTO} object
     * to the corresponding properties of a new {@code TvShow} object.
     *
     * @param tmdbTvShowDTO the {@code TmdbTvShowDTO} object containing the TV show details
     *                      retrieved from the TMDB API
     * @return a {@code TvShow} object populated with the mapped properties from the given
     *         {@code TmdbTvShowDTO}
     */
    private TvShow convertTmdbTvShowDtoToTvShow(TmdbTvShowDTO tmdbTvShowDTO) {
        return getTvShow(tmdbTvShowDTO);
    }

    /**
     * Saves a TV show to the database using its TMDB ID.
     *
     * This method first checks if a TV show with the specified TMDB ID already exists
     * in the database. If it does, a {@code MediaAlreadyExistsException} is thrown.
     * If not, the method fetches the TV show's details from the TMDB API, converts
     * them into a {@code TvShow} object, and saves the resulting object to the repository.
     *
     * @param tmdbId the unique identifier of the TV show in the TMDB database
     * @return the saved {@code TvShow} object
     * @throws MediaAlreadyExistsException if a TV show with the given TMDB ID already exists in the database
     */
    public TvShow saveTvShowToDb(Long tmdbId) {
        if (tvShowRepository.findByTmdbId(tmdbId).isPresent()) {
            throw new MediaAlreadyExistsException("TV show already added");
        }

        TvShow tvShow = convertTmdbTvShowDtoToTvShow(fetchTvShowDetails(tmdbId));
        return tvShowRepository.save(tvShow);
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

    /**
     * Retrieves a list of top-rated TV shows from the TMDB API.
     *
     * This method interacts with the TMDB client to fetch the current list
     * of TV shows that are highly rated based on the criteria established by TMDB.
     *
     * @return a list of {@code TmdbTvShowDTO} objects representing the top-rated TV shows.
     *         The returned list may be empty if no top-rated TV shows are found.
     */
    public List<TmdbTvShowDTO> topRatedTvShows() {
        return tmdbClient.topRatedTvShows();
    }
}
