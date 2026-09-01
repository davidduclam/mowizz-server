package com.github.davidduclam.movietracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

public record MovieResponseDTO(
    Long id,
    String title,
    String overview,
    LocalDate releaseDate,
    String posterPath,
    String backdropPath,
    Double voteAverage,
    @JsonInclude(JsonInclude.Include.NON_NULL) String trailerKey,
    @JsonInclude(JsonInclude.Include.NON_NULL) String logoPath
) {}
