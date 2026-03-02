package com.github.davidduclam.movietracker.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "mediaType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MovieWatchlistItemDTO.class, name = "MOVIE"),
        @JsonSubTypes.Type(value = TvShowWatchlistItemDTO.class, name = "TV")
})
public sealed interface WatchlistItemDTO permits MovieWatchlistItemDTO, TvShowWatchlistItemDTO {}
