package com.github.davidduclam.movietracker.dto;

import java.time.LocalDate;

public record MovieSearchResultDTO(
        Long id,
        String mediaType,
        String title,
        LocalDate releaseDate,
        String posterPath,
        Double voteAverage
) implements SearchResultDTO {}
