package com.github.davidduclam.movietracker.client.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

public record TmdbMultiSearchResponseDTO(
    int page,
    List<TmdbSearchResultDTO> results,
    @JsonProperty("total_pages") int totalPages,
    @JsonProperty("total_results") int totalResults
    
) {}


