package com.github.davidduclam.movietracker.client.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbTvShowResultDTO(
        Long id,
        @JsonProperty("media_type") String mediaType,
        @JsonProperty("poster_path") String posterPath,
        @JsonProperty("vote_average") double voteAverage,
        String name,
        @JsonProperty("first_air_date") String releaseDate

) implements TmdbSearchResultDTO {}
