package com.github.davidduclam.movietracker.client.tmdb.dto;

import java.time.LocalDate;

public record TmdbTvShowDTO(
        Long id,
        String name,
        String overview,
        LocalDate first_air_date,
        String poster_path,
        String backdrop_path,
        Double vote_average,
        TmdbVideoResultsDTO videos
) {}
