package com.github.davidduclam.movietracker.client.tmdb.dto;

import java.util.List;

public record TmdbImageResultsDTO(
        List<TmdbImageDTO> logos
) {
}
