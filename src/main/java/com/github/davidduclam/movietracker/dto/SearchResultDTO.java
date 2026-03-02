package com.github.davidduclam.movietracker.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "mediaType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MovieSearchResultDTO.class, name = "movie"),
        @JsonSubTypes.Type(value = TvShowSearchResultDTO.class, name = "tv")
})
public sealed interface SearchResultDTO permits MovieSearchResultDTO, TvShowSearchResultDTO {

}
