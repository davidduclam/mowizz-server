package com.github.davidduclam.movietracker.dto;

import java.time.LocalDate;

public record TvShowResponseDTO(
        Long id,
        String name,
        String overview,
        LocalDate firstAirDate,
        String posterPath,
        String backdropPath,
        Double voteAverage
) {}

