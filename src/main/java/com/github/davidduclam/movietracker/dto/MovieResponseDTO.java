package com.github.davidduclam.movietracker.dto;

import java.time.LocalDate;

public record MovieResponseDTO(
    Long id,
    String title,
    String overview,
    LocalDate releaseDate,
    String posterPath,
    String backdropPath,
    Double voteAverage,
    String trailerKey
) {}
