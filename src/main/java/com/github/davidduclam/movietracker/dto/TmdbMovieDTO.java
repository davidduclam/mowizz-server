package com.github.davidduclam.movietracker.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TmdbMovieDTO {

    private Long id;
    private String title;
    private LocalDate release_date;
    private String poster_path;
    private String overview;
    private Double vote_average;
}
