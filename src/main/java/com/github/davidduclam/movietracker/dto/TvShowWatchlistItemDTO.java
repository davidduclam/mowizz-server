package com.github.davidduclam.movietracker.dto;

import com.github.davidduclam.movietracker.model.MediaType;

import java.time.LocalDate;

public record TvShowWatchlistItemDTO(
        Long userMediaId,
        Long id,
        MediaType mediaType,
        String name,
        String overview,
        LocalDate firstAirDate,
        String posterPath,
        String backdropPath,
        Double voteAverage
) implements WatchlistItemDTO {}
