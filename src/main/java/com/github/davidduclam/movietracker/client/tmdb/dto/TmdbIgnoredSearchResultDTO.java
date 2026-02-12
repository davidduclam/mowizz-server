package com.github.davidduclam.movietracker.client.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbIgnoredSearchResultDTO(
        Long id,
        @JsonProperty("media_type") String mediaType
) implements TmdbSearchResultDTO {}
