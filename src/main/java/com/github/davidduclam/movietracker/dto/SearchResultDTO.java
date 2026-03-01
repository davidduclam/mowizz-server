package com.github.davidduclam.movietracker.dto;

public sealed interface SearchResultDTO permits MovieSearchResultDTO, TvShowSearchResultDTO {

}
