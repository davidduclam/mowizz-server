package com.github.davidduclam.movietracker.dto;

import lombok.Data;

import java.util.List;

@Data
public class TmdbSearchMovieResponseDTO {
    private int page;
    private List<TmdbMovieDTO> results;
    private int total_pages;
    private int total_results;
}
