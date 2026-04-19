package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.TmdbClient;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbVideoDTO;
import com.github.davidduclam.movietracker.dto.UserMediaRequestDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbTvShowDTO;
import com.github.davidduclam.movietracker.dto.TrailerDTO;
import com.github.davidduclam.movietracker.dto.TvShowResponseDTO;
import com.github.davidduclam.movietracker.error.MediaNotFoundException;
import com.github.davidduclam.movietracker.model.TvShow;
import com.github.davidduclam.movietracker.repository.TvShowRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TvShowService {
    private final TvShowRepository tvShowRepository;
    private final TmdbClient tmdbClient;

    public TvShowService(TvShowRepository tvShowRepository, TmdbClient tmdbClient) {
        this.tvShowRepository = tvShowRepository;
        this.tmdbClient = tmdbClient;
    }

    /**
     * Converts a {@code TvShowResponseDTO} object into a {@code TvShow} entity.
     *
     * This method maps the attributes from the given {@code TvShowResponseDTO},
     * such as TMDB ID, title, overview, first air date, poster path, and backdrop path,
     * into a new instance of the {@code TvShow} model.
     *
     * @param tvShowResponseDTO the DTO containing TV show details retrieved from an external API
     * @return a {@code TvShow} entity populated with the corresponding values from the {@code TvShowResponseDTO}
     */
    private TvShow convertTvShowResponseDtoToTvShow(TvShowResponseDTO tvShowResponseDTO) {
        TvShow tvShow = new TvShow();
        tvShow.setTmdbId(tvShowResponseDTO.id());
        tvShow.setTitle(tvShowResponseDTO.name());
        tvShow.setOverview(tvShowResponseDTO.overview());
        tvShow.setFirstAirDate(tvShowResponseDTO.firstAirDate());
        tvShow.setPosterPath(tvShowResponseDTO.posterPath());
        tvShow.setBackdropPath(tvShowResponseDTO.backdropPath());
        tvShow.setVoteAverage(tvShowResponseDTO.voteAverage());
        return tvShow;
    }

    /**
     * Saves a TV show to the database if it does not already exist.
     *
     * This method checks if a TV show with the given TMDB ID exists
     * in the database. If it does not, it fetches the TV show details
     * from an external API, converts the fetched data into a {@code TvShow}
     * entity, and saves it to the repository.
     *
     * @param userMediaRequestDTO the DTO containing the TMDB ID of the
     *                                TV show to be saved
     */
    public void saveTvShowToDb(UserMediaRequestDTO userMediaRequestDTO) {
        if (tvShowRepository.findByTmdbId(userMediaRequestDTO.tmdbId()).isEmpty()) {
            TvShow tvShow = convertTvShowResponseDtoToTvShow(fetchTvShowDetails(userMediaRequestDTO.tmdbId()));
            tvShowRepository.save(tvShow);
        }
    }

    /**
     * Retrieves a TV show from the database based on the provided TMDB ID.
     *
     * This method queries the database for a TV show associated with the given TMDB ID.
     * If a matching TV show is found, its details are mapped to a {@code TvShowResponseDTO}
     * and returned. If no matching TV show is found, a {@code MediaNotFoundException} is thrown.
     *
     * @param tmdbId the TMDB ID of the TV show to be retrieved
     * @return a {@code TvShowResponseDTO} containing the details of the retrieved TV show
     * @throws MediaNotFoundException if no TV show with the provided TMDB ID is found in the database
     */
    public TvShowResponseDTO getTvShowFromDb(Long tmdbId) {
        return tvShowRepository.findByTmdbId(tmdbId)
                .map(tvShow -> new TvShowResponseDTO(
                        tvShow.getTmdbId(), tvShow.getTitle(), tvShow.getOverview(),
                        tvShow.getFirstAirDate(), tvShow.getPosterPath(),
                        tvShow.getBackdropPath(), tvShow.getVoteAverage(), null))
                .orElseThrow(MediaNotFoundException::new);
    }

    /**
     * Fetches detailed information about a TV show from the TMDB API.
     *
     * This method interacts with the TMDB client to retrieve details
     * of a TV show corresponding to the given TMDB ID. The fetched details
     * are then transformed into a {@code TvShowResponseDTO} object.
     *
     * @param tmdbId the TMDB ID of the TV show to fetch details for
     * @return a {@code TvShowResponseDTO} containing detailed information
     *         about the specified TV show
     */
    public TvShowResponseDTO fetchTvShowDetails(Long tmdbId) {
        TmdbTvShowDTO tmdbTvShowDTO = tmdbClient.fetchTvShowDetails(tmdbId);
        return toTvShowResponse(tmdbTvShowDTO);
    }

    /**
     * Retrieves a list of popular TV shows from the TMDB API.
     *
     * This method interacts with the TMDB client to fetch the current list of TV shows
     * that are considered popular based on the API's criteria. The fetched data is
     * transformed into a list of {@code TvShowResponseDTO} objects.
     *
     * @return a list of {@code TvShowResponseDTO} objects representing popular TV shows.
     *         The returned list may be empty if no popular TV shows are found.
     */
    public List<TvShowResponseDTO> popularTvShows() {
        List<TmdbTvShowDTO> tmdbTvShowDTOList = tmdbClient.popularTvShows();
        return tmdbTvShowDTOList.stream().map(this::toTvShowResponse).toList();
    }

    /**
     * Retrieves a list of top-rated TV shows from the TMDB API.
     *
     * This method interacts with the TMDB client to fetch the current list
     * of TV shows that are highly rated based on the API's criteria. The
     * method then converts the results into a list of {@code TvShowResponseDTO}
     * objects.
     *
     * @return a list of {@code TvShowResponseDTO} objects representing the top-rated TV shows.
     *         The returned list may be empty if no top-rated TV shows are found.
     */
    public List<TvShowResponseDTO> topRatedTvShows() {
        List<TmdbTvShowDTO> tmdbTvShowDTOList = tmdbClient.topRatedTvShows();
        return tmdbTvShowDTOList.stream().map(this::toTvShowResponse).toList();
    }

    /**
     * Retrieves the official trailer for a TV show using its TMDB ID.
     *
     * @param tmdbId The unique ID of the TV show in TMDB.
     * @return A {@link TrailerDTO} representing the official trailer of the TV show.
     * @throws MediaNotFoundException if no official trailer is found for the given TV show.
     */
    public TrailerDTO getTvShowTrailer(Long tmdbId) {
        List<TmdbVideoDTO> tmdbVideoDTOList = tmdbClient.fetchTvShowTrailers(tmdbId);
        Optional<TrailerDTO> trailerDTO = tmdbVideoDTOList.stream().filter(f -> f.type().equals("Trailer") && f.official().equals(true)).map(this::toTrailer).findFirst();
        return trailerDTO.orElseThrow(MediaNotFoundException::new);
    }

    /**
     * Converts a TmdbVideoDTO object to a TrailerDTO object.
     *
     * @param tmdbVideoDTO the TmdbVideoDTO object containing video details
     * @return a TrailerDTO object populated with data from the given TmdbVideoDTO
     */
    private TrailerDTO toTrailer(TmdbVideoDTO tmdbVideoDTO) {
        return new TrailerDTO(
                tmdbVideoDTO.key(),
                tmdbVideoDTO.name(),
                tmdbVideoDTO.site()
        );
    }

    /**
     * Converts a TmdbTvShowDTO object into a TvShowResponseDTO object.
     *
     * @param tmdbTvShowDTO the input DTO containing data from the TMDB API about a TV show
     * @return a TvShowResponseDTO containing the mapped data, including the trailer key if available
     */
    private TvShowResponseDTO toTvShowResponse(TmdbTvShowDTO tmdbTvShowDTO) {
        String trailerKey = tmdbTvShowDTO.videos() == null ? null :
                tmdbTvShowDTO.videos().results().stream()
                        .filter(v -> "Trailer".equals(v.type()) && Boolean.TRUE.equals(v.official()))
                        .map(TmdbVideoDTO::key)
                        .findFirst()
                        .orElse(null);
        return new TvShowResponseDTO(
                tmdbTvShowDTO.id(),
                tmdbTvShowDTO.name(),
                tmdbTvShowDTO.overview(),
                tmdbTvShowDTO.first_air_date(),
                tmdbTvShowDTO.poster_path(),
                tmdbTvShowDTO.backdrop_path(),
                tmdbTvShowDTO.vote_average(),
                trailerKey);
    }
}
