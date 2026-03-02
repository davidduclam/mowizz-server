package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.dto.*;
import com.github.davidduclam.movietracker.error.MediaAlreadyExistsException;
import com.github.davidduclam.movietracker.error.MediaNotFoundException;
import com.github.davidduclam.movietracker.error.UserNotFoundException;
import com.github.davidduclam.movietracker.model.*;
import com.github.davidduclam.movietracker.repository.UserMediaRepository;
import com.github.davidduclam.movietracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

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
                .orElseThrow(UserNotFoundException::new);

        saveMediaToMovieOrTvShowDb(addUserMediaRequestDTO);

        if (userMediaRepository.existsByUserIdAndMediaTypeAndTmdbId(userId, addUserMediaRequestDTO.mediaType(), addUserMediaRequestDTO.tmdbId())) {
            throw new MediaAlreadyExistsException();
        }

        return saveUserMediaToDb(user, addUserMediaRequestDTO);
    }

    /**
     * Retrieves a list of media items associated with a specific user.
     * The list is sorted in descending order by the media item's ID.
     * Media items can be of type Movie or TV Show, and additional details
     * are fetched based on the provided media type.
     *
     * @param userId the unique identifier of the user whose media items are to be retrieved
     * @return a list of media items (WatchlistItemDTO) belonging to the user
     * @throws UserNotFoundException if the user with the specified ID is not found
     */
    public List<WatchlistItemDTO> getMediaFromUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return userMediaRepository.findUserMediaByUserId(userId).stream()
                .sorted(Comparator.comparing(UserMedia::getId).reversed())
                .map(userMedia -> {
                    if (userMedia.getMediaType() == MediaType.MOVIE) {
                        return toWatchlistItem(userMedia, movieService.fetchMovieDetails(userMedia.getTmdbId()));
                    } else {
                        return toWatchlistItem(userMedia, tvShowService.fetchTvShowDetails(userMedia.getTmdbId()));
                    }
                })
                .toList();
    }

    /**
     * Deletes a specific media item associated with a given user.
     *
     * @param userId the ID of the user whose media is to be deleted
     * @param mediaId the ID of the media to be deleted
     * @throws UserNotFoundException if the user with the specified ID is not found
     * @throws MediaNotFoundException if the media with the specified ID is not found for the user
     */
    public void deleteMediaFromUser(Long userId, Long mediaId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        UserMedia userMedia = userMediaRepository
                .findUserMediaByUserId(userId)
                .stream().filter(m -> m.getId().equals(mediaId))
                .findFirst()
                .orElseThrow(MediaNotFoundException::new);

        userMediaRepository.deleteById(mediaId);
    }

    /**
     * Converts a UserMedia object and a MovieResponseDTO object into a WatchlistItemDTO.
     *
     * @param userMedia the user-specific media entity containing user-specific media information
     * @param movieResponseDTO the movie response data transfer object containing movie-related details
     * @return a WatchlistItemDTO representing the movie in the user's watchlist
     */
    private WatchlistItemDTO toWatchlistItem(UserMedia userMedia, MovieResponseDTO movieResponseDTO) {
        return new MovieWatchlistItemDTO(
                userMedia.getId(),
                movieResponseDTO.id(),
                MediaType.MOVIE,
                movieResponseDTO.title(),
                movieResponseDTO.overview(),
                movieResponseDTO.releaseDate(),
                movieResponseDTO.posterPath(),
                movieResponseDTO.backdropPath(),
                movieResponseDTO.voteAverage()
        );
    }

    /**
     * Converts user media data and TV show response data into a WatchlistItemDTO.
     *
     * @param userMedia the user media entity containing user-specific information
     * @param tvShowResponseDTO the TV show response data containing details about the TV show
     * @return a WatchlistItemDTO representing the TV show and user-specific information in the watchlist
     */
    private WatchlistItemDTO toWatchlistItem(UserMedia userMedia, TvShowResponseDTO tvShowResponseDTO) {
        return new TvShowWatchlistItemDTO(
                userMedia.getId(),
                tvShowResponseDTO.id(),
                MediaType.TV,
                tvShowResponseDTO.name(),
                tvShowResponseDTO.overview(),
                tvShowResponseDTO.firstAirDate(),
                tvShowResponseDTO.posterPath(),
                tvShowResponseDTO.backdropPath(),
                tvShowResponseDTO.voteAverage()
        );
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
        if (addUserMediaRequestDTO.mediaType() == null) {
            throw new IllegalArgumentException("mediaType is required");
        }

        if (addUserMediaRequestDTO.mediaType() == MediaType.MOVIE) {
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
        userMedia.setTmdbId(addUserMediaRequestDTO.tmdbId());
        userMedia.setMediaType(addUserMediaRequestDTO.mediaType());

        userMediaRepository.save(userMedia);
        return userMedia;
    }
}
