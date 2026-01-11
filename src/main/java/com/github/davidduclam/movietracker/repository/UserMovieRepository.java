package com.github.davidduclam.movietracker.repository;

import com.github.davidduclam.movietracker.model.UserMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMovieRepository extends JpaRepository<UserMovie, Long> {

    //List<UserMovie> findByUserId(Long userId);

    //boolean existsByUserIdAndMovieTmdbId(Long userId, Long tmdbId);

    //void deleteByUserIdAndMovieId(Long userId, Long tmdbId);

}
