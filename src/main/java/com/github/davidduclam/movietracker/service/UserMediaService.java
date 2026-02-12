package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.dto.AddUserMediaRequestDTO;
import com.github.davidduclam.movietracker.error.MediaAlreadyExistsException;
import com.github.davidduclam.movietracker.error.UserNotFoundException;
import com.github.davidduclam.movietracker.model.*;
import com.github.davidduclam.movietracker.repository.UserMediaRepository;
import com.github.davidduclam.movietracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Adds media (movie or TV show) to a user's collection and persists it in the database.
     *
     * This method performs the following steps:
     * - Retrieves the user by their ID from the database.
     * - Saves media information to the respective database (Movie or TV Show).
     * - Checks if the specified media already exists in the user's collection.
     * - Saves the UserMedia entity associating the user with the specified media.
     *
     * @param userId The unique identifier of the user to whom the media is to be added.
     * @param addUserMediaRequestDTO A data transfer object containing the information about the media
     *                                to be added, including its TMDb ID and media type (e.g., movie or TV show).
     * @return The UserMedia entity representing the association between the user and the added media.
     * @throws UserNotFoundException If the specified user does not exist.
     * @throws MediaAlreadyExistsException If the media has already been added to the user's collection.
     */
    @Transactional
    public UserMedia addMediaToUser(Long userId, AddUserMediaRequestDTO addUserMediaRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        saveMediaToMovieOrTvShowDb(addUserMediaRequestDTO);

        if (userMediaRepository.existsByUserIdAndMediaTypeAndTmdbId(userId, addUserMediaRequestDTO.getMediaType(), addUserMediaRequestDTO.getTmdbId())) {
            throw new MediaAlreadyExistsException("Media already added");
        }

        return saveUserMediaToDb(user, addUserMediaRequestDTO);
    }

    /**
     * Saves a new UserMedia entity to the database based on the provided user and request DTO.
     * Delegates the saving logic to the {@code addUserMediaToDb} method.
     *
     * @param user The User entity associated with the UserMedia to be added.
     * @param addUserMediaRequestDTO The data transfer object containing information required to create the UserMedia entity.
     * @return The UserMedia entity that was saved to the database.
     */
    private UserMedia saveUserMediaToDb(User user, AddUserMediaRequestDTO addUserMediaRequestDTO) {
        return addUserMediaToDb(user, addUserMediaRequestDTO);
    }

    /**
     * Saves media information (movie or TV show) to the database based on the provided request DTO.
     * Determines the type of media from the {@code mediaType} field in the DTO and delegates the
     * save operation to the appropriate service.
     *
     * @param addUserMediaRequestDTO the data transfer object containing details of the media to be saved,
     *                               including its type and TMDb ID
     * @throws IllegalArgumentException if the {@code mediaType} field in the request DTO is null
     */
    private void saveMediaToMovieOrTvShowDb(AddUserMediaRequestDTO addUserMediaRequestDTO) {
        if (addUserMediaRequestDTO.getMediaType() == null) {
            throw new IllegalArgumentException("mediaType is required");
        }

        if (addUserMediaRequestDTO.getMediaType() == MediaType.MOVIE) {
            movieService.saveMovieToDb(addUserMediaRequestDTO);
        } else {
            tvShowService.saveTvShowToDb(addUserMediaRequestDTO);
        }
    }

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
