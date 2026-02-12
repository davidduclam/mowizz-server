package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.dto.AddUserMediaRequestDTO;
import com.github.davidduclam.movietracker.dto.TmdbMovieDTO;
import com.github.davidduclam.movietracker.dto.TmdbTvShowDTO;
import com.github.davidduclam.movietracker.error.MediaAlreadyExistsException;
import com.github.davidduclam.movietracker.error.UserNotFoundException;
import com.github.davidduclam.movietracker.model.Movie;
import com.github.davidduclam.movietracker.model.TvShow;
import com.github.davidduclam.movietracker.model.User;
import com.github.davidduclam.movietracker.model.UserMedia;
import com.github.davidduclam.movietracker.repository.UserMediaRepository;
import com.github.davidduclam.movietracker.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserMediaService {
    private final UserMediaRepository userMediaRepository;
    private final UserRepository userRepository;
    private final MovieService movieService;
    private final TvShowService tvShowService;

    public UserMediaService(UserMediaRepository userMediaRepository, UserRepository userRepository, MovieService movieService, TvShowService tvShowService) {
        this.userMediaRepository = userMediaRepository;
        this.userRepository = userRepository;
        this.movieService = movieService;
        this.tvShowService = tvShowService;
    }

    // =======================================================
    // Movie helpers
    // =======================================================

    /**
     * Saves a movie to the user's media list in the database. If the user does not exist,
     * throws a {@code UserNotFoundException}. If the movie is already in the user's media list,
     * throws a {@code MediaAlreadyExistsException}.
     *
     * @param userId                 the ID of the user to whom the media belongs
     * @param addUserMediaRequestDTO the data transfer object containing details of the media to be added
     * @return a {@code UserMedia} object representing the saved media entry
     * @throws UserNotFoundException       if the user with the specified {@code userId} does not exist
     * @throws MediaAlreadyExistsException if the movie is already present in the user's media list
     */
    public UserMedia saveUserMediaMovieToDb(Long userId, AddUserMediaRequestDTO addUserMediaRequestDTO) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        if (userMediaRepository.existsByUserIdAndMediaTypeAndTmdbId(userId, addUserMediaRequestDTO.getMediaType(), addUserMediaRequestDTO.getTmdbId())) {
            throw new MediaAlreadyExistsException("Movie already added");
        } else {
            return addUserMediaToDb(user.get(), addUserMediaRequestDTO);
        }
    }

    /**
     * Saves a movie to the database after verifying that it doesn't already exist.
     * If the movie is not found in the database, it fetches the movie details from the external
     * TMDB API, converts the DTO into a Movie entity, and saves it in the repository.
     *
     * @param addUserMediaRequestDTO the data transfer object containing the TMDB ID of the movie
     *                               and other relevant information
     */
    public void saveMovieToDb(AddUserMediaRequestDTO addUserMediaRequestDTO) {
        movieService.saveMovieToDb(addUserMediaRequestDTO);
    }

    // =======================================================
    // TV show helpers
    // =======================================================

    /**
     * Saves a TV show to the user's media list in the database. If the user does not exist,
     * throws a {@code UserNotFoundException}. If the TV show is already in the user's media list,
     * throws a {@code MediaAlreadyExistsException}.
     *
     * @param userId                 the ID of the user to whom the media belongs
     * @param addUserMediaRequestDTO the data transfer object containing details of the media to be added
     * @return a {@code UserMedia} object representing the saved media entry
     * @throws UserNotFoundException       if the user with the specified {@code userId} does not exist
     * @throws MediaAlreadyExistsException if the TV show is already present in the user's media list
     */
    public UserMedia saveUserMediaTvShowToDb(Long userId, AddUserMediaRequestDTO addUserMediaRequestDTO) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        if (userMediaRepository.existsByUserIdAndMediaTypeAndTmdbId(userId, addUserMediaRequestDTO.getMediaType(), addUserMediaRequestDTO.getTmdbId())) {
            throw new MediaAlreadyExistsException("TV show already added");
        } else {
            return addUserMediaToDb(user.get(), addUserMediaRequestDTO);
        }
    }

    /**
     * Saves a TV show to the database if it does not already exist.
     *
     * @param addUserMediaRequestDTO the data transfer object containing details
     *                               related to the TV show, including its TMDB ID
     */
    public void saveTvShowToDb(AddUserMediaRequestDTO addUserMediaRequestDTO) {
        tvShowService.saveTvShowToDb(addUserMediaRequestDTO);
    }

    // =======================================================
    // Internal helpers
    // =======================================================

    /**
     * Adds a new UserMedia entity to the database based on the provided user and request DTO.
     *
     * @param user The User entity associated with the UserMedia to be added.
     * @param addUserMediaRequestDTO The data transfer object containing information required to create the UserMedia entity.
     * @return The UserMedia entity that was saved to the database.
     */
    private UserMedia addUserMediaToDb(User user, AddUserMediaRequestDTO addUserMediaRequestDTO) {
        UserMedia userMedia = new UserMedia();
        userMedia.setUser(user);
        userMedia.setTmdbId(addUserMediaRequestDTO.getTmdbId());
        userMedia.setMediaType(addUserMediaRequestDTO.getMediaType());

        userMediaRepository.save(userMedia);
        return userMedia;
    }
}
