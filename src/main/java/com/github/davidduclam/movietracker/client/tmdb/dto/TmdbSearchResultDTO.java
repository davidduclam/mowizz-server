package com.github.davidduclam.movietracker.client.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "media_type",
        visible = true
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = TmdbMovieResultDTO.class, name = "movie"),
        @JsonSubTypes.Type(value = TmdbTvShowResultDTO.class, name = "tv")

})
public sealed interface TmdbSearchResultDTO permits TmdbMovieResultDTO, TmdbTvShowResultDTO {
    Long id();
    @JsonProperty("media_type")
    String mediaType();
    @JsonProperty("poster_path")
    String posterPath();
    @JsonProperty("vote_average")
    double voteAverage();
}
