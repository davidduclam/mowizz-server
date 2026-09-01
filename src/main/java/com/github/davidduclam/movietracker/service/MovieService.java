package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.TmdbClient;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbVideoDTO;
import com.github.davidduclam.movietracker.dto.UserMediaRequestDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbMovieDTO;
import com.github.davidduclam.movietracker.dto.MovieResponseDTO;
import com.github.davidduclam.movietracker.dto.TrailerDTO;
import com.github.davidduclam.movietracker.error.MediaNotFoundException;
import com.github.davidduclam.movietracker.model.Movie;
import com.github.davidduclam.movietracker.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
     * Converts a MovieResponseDTO object containing movie details fetched from TMDb into a Movie entity.
     * This method maps the fields from the DTO to the corresponding fields in the Movie entity.
     *
     * @param movieResponseDTO the MovieResponseDTO object containing details about a movie,
     *                         such as its ID, title, release date, poster path, backdrop path, and overview.
     * @return a Movie entity populated with data from the provided MovieResponseDTO.
     */
    private Movie convertMovieResponseDtoToMovie(MovieResponseDTO movieResponseDTO) {
        Movie movie = new Movie();
        movie.setTmdbId(movieResponseDTO.id());
        movie.setTitle(movieResponseDTO.title());
        movie.setReleaseDate(movieResponseDTO.releaseDate());
        movie.setPosterPath(movieResponseDTO.posterPath());
        movie.setBackdropPath(movieResponseDTO.backdropPath());
        movie.setOverview(movieResponseDTO.overview());
        movie.setVoteAverage(movieResponseDTO.voteAverage());
        return movie;
    }

    /**
     * Saves a movie to the database if it does not already exist.
     * This method checks if a movie with the provided TMDb ID is already present in the repository.
     * If not, it fetches the movie details from TMDb, converts the details to a Movie entity,
     * and saves it to the database.
     *
     * @param userMediaRequestDTO the data transfer object containing information about the movie to be added,
     *                               including the TMDb ID required to fetch the movie details.
     */
    public void saveMovieToDb(UserMediaRequestDTO userMediaRequestDTO) {
        if (movieRepository.findByTmdbId(userMediaRequestDTO.tmdbId()).isEmpty()) {
            Movie movie = convertMovieResponseDtoToMovie(fetchMovieDetails(userMediaRequestDTO.tmdbId()));
            movieRepository.save(movie);
        }
    }

    /**
     * Retrieves movie details from the database using the provided TMDb ID.
     * If the movie is found, it is mapped to a MovieResponseDTO object.
     * If the movie is not found, a MediaNotFoundException is thrown.
     *
     * @param tmdbId the ID of the movie in the TMDb database
     * @return a MovieResponseDTO object containing the movie's details,
     *         including its ID, title, overview, release date, poster path,
     *         and backdrop path
     * @throws MediaNotFoundException if the movie with the given TMDb ID is not found
     */
    public MovieResponseDTO getMovieFromDb(Long tmdbId) {
        return movieRepository.findByTmdbId(tmdbId)
                .map(movie -> new MovieResponseDTO(
                        movie.getTmdbId(), movie.getTitle(), movie.getOverview(),
                        movie.getReleaseDate(), movie.getPosterPath(),
                        movie.getBackdropPath(), movie.getVoteAverage(), null, null))
                .orElseThrow(MediaNotFoundException::new);
    }

    /**
     * Fetches movie details from TMDb using the provided TMDb ID and returns a MovieResponseDTO object
     * containing the movie's information.
     *
     * @param tmdbId the ID of the movie from TMDb
     * @return a MovieResponseDTO object containing the movie's details, including its ID, title, release date,
     *         poster path, backdrop path, overview, and vote average
     */
    public MovieResponseDTO fetchMovieDetails(Long tmdbId) {
        TmdbMovieDTO tmdbMovieDTO = tmdbClient.fetchMovieDetails(tmdbId);
        return toMovieResponse(tmdbMovieDTO);
    }

    /**
     * Retrieves a list of popular movies from TMDb and converts the results
     * into a list of MovieResponseDTO objects.
     *
     * @return a list of MovieResponseDTO objects representing the popular movies,
     *         including their ID, title, release date, poster path, backdrop path,
     *         overview, and vote average.
     */
    public List<MovieResponseDTO> popularMovies() {
        List<TmdbMovieDTO> tmdbMovieDTOList = tmdbClient.popularMovies();
        return tmdbMovieDTOList.stream().map(this::toMovieResponse).toList();
    }

    /**
     * Retrieves a list of top-rated movies from TMDb and converts the results
     * into a list of MovieResponseDTO objects.
     *
     * @return a list of MovieResponseDTO objects representing the top-rated movies,
     *         including their ID, title, release date, poster path, backdrop path,
     *         overview, and vote average.
     */
    public List<MovieResponseDTO> topRatedMovies() {
        List<TmdbMovieDTO> tmdbMovieDTOList = tmdbClient.topRatedMovies();
        return tmdbMovieDTOList.stream().map(this::toMovieResponse).toList();
    }

    /**
     * Fetches a list of upcoming movies using the TmdbClient and converts the results
     * into a list of MovieResponseDTO objects.
     *
     * @return a list of MovieResponseDTO objects containing details of upcoming movies,
     *         such as their ID, title, release date, poster path, backdrop path,
     *         overview, and vote average
     */
    public List<MovieResponseDTO> upcomingMovies() {
        List<TmdbMovieDTO> tmdbMovieDTOList = tmdbClient.upcomingMovies();
        return tmdbMovieDTOList.stream().map(this::toMovieResponse).toList();
    }

    /**
     * Retrieves the movie trailer for a given TMDb movie ID.
     *
     * @param tmdbId the ID of the TMDb movie for which the trailer is being fetched
     * @return the trailer information encapsulated in a TrailerDTO object
     * @throws MediaNotFoundException if no official trailer is found for the given movie ID
     */
    public TrailerDTO getMovieTrailer(Long tmdbId) {
        List<TmdbVideoDTO> tmdbVideoDTOList = tmdbClient.fetchMovieTrailers(tmdbId);
        Optional<TrailerDTO> trailerDTO = tmdbVideoDTOList.stream()
                .filter(f -> f.type().equals("Trailer") && f.official().equals(true))
                .map(TmdbResponseMapper::toTrailer)
                .findFirst();
        return trailerDTO.orElseThrow(MediaNotFoundException::new);
    }

    /**
     * Converts a TmdbMovieDTO object to a MovieResponseDTO object.
     *
     * @param m the TmdbMovieDTO object containing movie details.
     * @return a MovieResponseDTO object mapped from the given TmdbMovieDTO.
     */
    private MovieResponseDTO toMovieResponse(TmdbMovieDTO m) {
        return new MovieResponseDTO(
                m.id(), m.title(), m.overview(), m.release_date(),
                m.poster_path(), m.backdrop_path(), m.vote_average(),
                TmdbResponseMapper.officialTrailerKey(m.videos()),
                TmdbResponseMapper.firstLogoPath(m.images()));
    }
}
