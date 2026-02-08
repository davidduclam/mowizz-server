package com.github.davidduclam.movietracker.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TmdbTvShowDTO {

    private Long id;
    private String name;
    private String overview;
    private LocalDate first_air_date;
    private String poster_path;
    private String backdrop_path;
    private Double vote_average;
}
