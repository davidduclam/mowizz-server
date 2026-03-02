package com.github.davidduclam.movietracker.dto;

import com.github.davidduclam.movietracker.model.MediaType;

import java.time.LocalDate;

public record MovieWatchlistItemDTO(
        Long userMediaId,
        Long id,
        MediaType mediaType,
        String title,
        String overview,
        LocalDate releaseDate,
        String posterPath,
        String backdropPath,
        Double voteAverage
) implements WatchlistItemDTO {}
