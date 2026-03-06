package com.github.davidduclam.movietracker.controller;

import com.github.davidduclam.movietracker.dto.TrailerDTO;
import com.github.davidduclam.movietracker.dto.TvShowResponseDTO;
import com.github.davidduclam.movietracker.service.TvShowService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
public class TvShowController {
    private final TvShowService tvShowService;

    public TvShowController(TvShowService tvShowService) {
        this.tvShowService = tvShowService;
    }

    @GetMapping("/{tmdbId}")
    public TvShowResponseDTO fetchTvShowDetails(@PathVariable Long tmdbId) {
        return tvShowService.fetchTvShowDetails(tmdbId);
    }

    @GetMapping("/{tmdbId}/trailer")
    public TrailerDTO fetchTrailer(@PathVariable Long tmdbId) {
        return tvShowService.getTvShowTrailer(tmdbId);
    }

    @GetMapping("/popular")
    public List<TvShowResponseDTO> popularTvShows() {
        return tvShowService.popularTvShows();
    }

    @GetMapping("/top-rated")
    public List<TvShowResponseDTO> topRatedTvShows() {
        return tvShowService.topRatedTvShows();
    }
}
