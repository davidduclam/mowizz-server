package com.github.davidduclam.movietracker.client.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbMovieResultDTO(
        Long id,
        @JsonProperty("media_type") String mediaType,
        @JsonProperty("poster_path") String posterPath,
        @JsonProperty("vote_average") double voteAverage,
        String title,
        @JsonProperty("release_date") String releaseDate

) implements TmdbSearchResultDTO {}
