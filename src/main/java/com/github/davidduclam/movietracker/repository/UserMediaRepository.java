package com.github.davidduclam.movietracker.repository;

import com.github.davidduclam.movietracker.model.UserMedia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMediaRepository extends JpaRepository<UserMedia, Long> {

    //List<UserMedia> findByUserId(Long userId);

    //boolean existsByUserIdAndMovieTmdbId(Long userId, Long tmdbId);

    //void deleteByUserIdAndMovieId(Long userId, Long tmdbId);

}
