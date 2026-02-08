package com.github.davidduclam.movietracker.dto;

import lombok.Data;

import java.util.List;

@Data
public class TmdbSearchTvShowResponseDTO {
    private int page;
    private List<TmdbTvShowDTO> results;
    private int total_pages;
    private int total_results;
}
