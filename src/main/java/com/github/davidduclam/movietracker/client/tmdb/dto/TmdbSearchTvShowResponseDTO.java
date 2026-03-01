package com.github.davidduclam.movietracker.client.tmdb.dto;

import java.util.List;

public record TmdbSearchTvShowResponseDTO(
        int page,
        List<TmdbTvShowDTO> results,
        int total_pages,
        int total_results
) {}
