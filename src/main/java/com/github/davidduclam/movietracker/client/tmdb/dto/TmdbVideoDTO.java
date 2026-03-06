package com.github.davidduclam.movietracker.client.tmdb.dto;

public record TmdbVideoDTO(
        String name,
        Boolean official,
        String key,
        String site,
        String type
) {
}
