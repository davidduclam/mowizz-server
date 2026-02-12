package com.github.davidduclam.movietracker.repository;

import com.github.davidduclam.movietracker.model.MediaType;
import com.github.davidduclam.movietracker.model.UserMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMediaRepository extends JpaRepository<UserMedia, Long> {

    List<UserMedia> findUserMediaByUserId(Long userId);

    boolean existsByUserIdAndMediaTypeAndTmdbId(Long user_id, MediaType mediaType, Long tmdbId);
}
