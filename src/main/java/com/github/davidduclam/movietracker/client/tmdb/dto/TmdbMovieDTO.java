package com.github.davidduclam.movietracker.client.tmdb.dto;

import java.time.LocalDate;

public record TmdbMovieDTO(
    Long id,
    String title,
    LocalDate release_date,
    String poster_path,
    String backdrop_path,
    String overview,
    Double vote_average
) {}
