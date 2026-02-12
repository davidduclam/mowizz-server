package com.github.davidduclam.movietracker.repository;

import com.github.davidduclam.movietracker.model.TvShow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TvShowRepository extends JpaRepository<TvShow, Long> {

    Optional<TvShow> findByTmdbId(Long tmdbId);

}
