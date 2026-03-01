package com.github.davidduclam.movietracker.client.tmdb.dto;

import java.util.List;

public record TmdbSearchMovieResponseDTO(
        int page,
        List<TmdbMovieDTO> results,
        int total_pages,
        int total_results
) {}
