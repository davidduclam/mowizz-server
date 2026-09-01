package com.github.davidduclam.movietracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

public record TvShowResponseDTO(
        Long id,
        String name,
        String overview,
        LocalDate firstAirDate,
        String posterPath,
        String backdropPath,
        Double voteAverage,
        @JsonInclude(JsonInclude.Include.NON_NULL) String trailerKey,
        @JsonInclude(JsonInclude.Include.NON_NULL) String logoPath
) {}

