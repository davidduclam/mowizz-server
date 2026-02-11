package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.TmdbClient;
import com.github.davidduclam.movietracker.controller.UserMediaController;
import com.github.davidduclam.movietracker.dto.TmdbMovieDTO;
import com.github.davidduclam.movietracker.dto.TmdbTvShowDTO;
import com.github.davidduclam.movietracker.error.MediaAlreadyExistsException;
import com.github.davidduclam.movietracker.error.UserNotFoundException;
import com.github.davidduclam.movietracker.model.Movie;
import com.github.davidduclam.movietracker.model.TvShow;
import com.github.davidduclam.movietracker.model.User;
import com.github.davidduclam.movietracker.model.UserMedia;
import com.github.davidduclam.movietracker.repository.MovieRepository;
import com.github.davidduclam.movietracker.repository.TvShowRepository;
import com.github.davidduclam.movietracker.repository.UserMediaRepository;
import com.github.davidduclam.movietracker.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserMediaService {
    private final UserMediaRepository userMediaRepository;
    private final MovieRepository movieRepository;
    private final TvShowRepository tvShowRepository;
    private final UserRepository userRepository;
    private final TmdbClient tmdbClient;

    public UserMediaService(UserMediaRepository userMediaRepository, MovieRepository movieRepository, TvShowRepository tvShowRepository, UserRepository userRepository, TmdbClient tmdbClient) {
        this.userMediaRepository = userMediaRepository;
        this.movieRepository = movieRepository;
        this.tvShowRepository = tvShowRepository;
        this.userRepository = userRepository;
        this.tmdbClient = tmdbClient;
    }

    // =======================================================
    // Movie helpers
    // =======================================================

    private Movie convertTmdbMovieDtoToMovie(TmdbMovieDTO tmdbMovieDTO) {
        return getMovie(tmdbMovieDTO);
    }

    public TmdbMovieDTO fetchMovieDetails(Long tmdbId) {
        return tmdbClient.fetchMovieDetails(tmdbId);
    }

    @NonNull
    static Movie getMovie(TmdbMovieDTO tmdbMovieDTO) {
        Movie movie = new Movie();
        movie.setTmdbId(tmdbMovieDTO.getId());
        movie.setTitle(tmdbMovieDTO.getTitle());
        movie.setReleaseDate(tmdbMovieDTO.getRelease_date());
        movie.setPosterPath(tmdbMovieDTO.getPoster_path());
        movie.setBackdropPath(tmdbMovieDTO.getBackdrop_path());
        movie.setOverview(tmdbMovieDTO.getOverview());
        return movie;
    }

    public UserMedia saveUserMediaMovieToDb(Long userId, Long tmdbdId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            UserMedia userMedia = new UserMedia();
            userMedia.setUser(user.get());
            userMedia.setTmdbId(tmdbdId);
            userMedia.setWatched(false);
            userMedia.setPersonalRating(0.0);
            userMedia.setWatchDate(null);
            userMedia.setMediaType("movie");

            userMediaRepository.save(userMedia);
            return userMedia;
        }
        throw new UserNotFoundException("User not found");
    }

    public void saveMovieToDb(Long tmdbId) {
        if (movieRepository.findByTmdbId(tmdbId).isPresent()) {
            throw new MediaAlreadyExistsException("Media already added");
        }

        Movie movie = convertTmdbMovieDtoToMovie(fetchMovieDetails(tmdbId));
         movieRepository.save(movie);
    }

    // =======================================================
    // TV show helpers
    // =======================================================

    private TvShow convertTmdbTvShowDtoToTvShow(TmdbTvShowDTO tmdbTvShowDTO) {
        return getTvShow(tmdbTvShowDTO);
    }

    public TmdbTvShowDTO fetchTvShowDetails(Long tmdbId) {
        return tmdbClient.fetchTvShowDetails(tmdbId);
    }

    @NonNull
    static TvShow getTvShow(TmdbTvShowDTO tmdbTvShowDTO) {
        TvShow tvShow = new TvShow();
        tvShow.setTmdbId(tmdbTvShowDTO.getId());
        tvShow.setTitle(tmdbTvShowDTO.getName());
        tvShow.setFirstAirDate(tmdbTvShowDTO.getFirst_air_date());
        tvShow.setPosterPath(tmdbTvShowDTO.getPoster_path());
        tvShow.setBackdropPath(tmdbTvShowDTO.getBackdrop_path());
        tvShow.setOverview(tmdbTvShowDTO.getOverview());
        return tvShow;
    }

    public UserMedia saveUserMediaTvShowToDb(Long userId, Long tmdbdId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            UserMedia userMedia = new UserMedia();
            userMedia.setUser(user.get());
            userMedia.setTmdbId(tmdbdId);
            userMedia.setWatched(false);
            userMedia.setPersonalRating(0.0);
            userMedia.setWatchDate(null);
            userMedia.setMediaType("tv");

            userMediaRepository.save(userMedia);
            return userMedia;
        }
        throw new UserNotFoundException("User not found");
    }

    public void saveTvShowToDb(Long tmdbId) {
        if (tvShowRepository.findByTmdbId(tmdbId).isPresent()) {
            throw new MediaAlreadyExistsException("TV show already added");
        }

        TvShow tvShow  = convertTmdbTvShowDtoToTvShow(fetchTvShowDetails(tmdbId));
        tvShowRepository.save(tvShow);
    }
}
