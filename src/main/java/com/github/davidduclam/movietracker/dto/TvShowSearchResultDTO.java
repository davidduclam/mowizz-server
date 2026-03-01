package com.github.davidduclam.movietracker.dto;

import java.time.LocalDate;

public record TvShowSearchResultDTO(
        Long id,
        String mediaType,
        String name,
        LocalDate firstAirDate,
        String posterPath,
        Double voteAverage
) implements SearchResultDTO {}
