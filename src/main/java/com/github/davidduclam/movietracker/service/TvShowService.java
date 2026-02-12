package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.TmdbClient;
import com.github.davidduclam.movietracker.dto.AddUserMediaRequestDTO;
import com.github.davidduclam.movietracker.dto.TmdbTvShowDTO;
import com.github.davidduclam.movietracker.model.TvShow;
import com.github.davidduclam.movietracker.repository.TvShowRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TvShowService {
    private final TvShowRepository tvShowRepository;
    private final TmdbClient tmdbClient;

    public TvShowService(TvShowRepository tvShowRepository, TmdbClient tmdbClient) {
        this.tvShowRepository = tvShowRepository;
        this.tmdbClient = tmdbClient;
    }

    /**
     * Converts a {@code TmdbTvShowDTO} object into a {@code TvShow} entity.
     *
     * This method copies relevant attributes from the {@code TmdbTvShowDTO} object
     * such as TMDB ID, name, first air date, poster path, backdrop path, and overview,
     * and sets them into a newly created {@code TvShow} entity.
     *
     * @param tmdbTvShowDTO the DTO containing details of the TV show retrieved from the TMDB API
     * @return a {@code TvShow} entity populated with the corresponding values from the {@code TmdbTvShowDTO}
     */
    private TvShow convertTmdbTvShowDtoToTvShow(TmdbTvShowDTO tmdbTvShowDTO) {
        TvShow tvShow = new TvShow();
        tvShow.setTmdbId(tmdbTvShowDTO.getId());
        tvShow.setTitle(tmdbTvShowDTO.getName());
        tvShow.setFirstAirDate(tmdbTvShowDTO.getFirst_air_date());
        tvShow.setPosterPath(tmdbTvShowDTO.getPoster_path());
        tvShow.setBackdropPath(tmdbTvShowDTO.getBackdrop_path());
        tvShow.setOverview(tmdbTvShowDTO.getOverview());
        return tvShow;
    }

    /**
     * Saves a TV show to the database if it does not already exist.
     *
     * This method checks if a TV show with the given TMDB ID already exists in the database.
     * If it does not exist, the method fetches the TV show details from the TMDB API, converts
     * the details into a {@code TvShow} entity, and saves it to the database.
     *
     * @param addUserMediaRequestDTO a DTO containing the media-related information, including
     *                               the TMDB ID of the TV show
     */
    public void saveTvShowToDb(AddUserMediaRequestDTO addUserMediaRequestDTO) {
        if (tvShowRepository.findByTmdbId(addUserMediaRequestDTO.getTmdbId()).isEmpty()) {
            TvShow tvShow = convertTmdbTvShowDtoToTvShow(fetchTvShowDetails(addUserMediaRequestDTO.getTmdbId()));
            tvShowRepository.save(tvShow);
        }
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
