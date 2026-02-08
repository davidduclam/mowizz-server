package com.github.davidduclam.movietracker.controller;

import com.github.davidduclam.movietracker.dto.TmdbTvShowDTO;
import com.github.davidduclam.movietracker.service.TvShowService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shows")
public class TvShowController {
    private final TvShowService tvShowService;

    public TvShowController(TvShowService tvShowService) {
        this.tvShowService = tvShowService;
    }

    @GetMapping("/{tmdbId}")
    public TmdbTvShowDTO fetchTvShowDetails(@PathVariable Long tmdbId) {
        return tvShowService.fetchTvShowDetails(tmdbId);
    }

    @GetMapping("/popular")
    public List<TmdbTvShowDTO> popularTvShows() {
        return tvShowService.popularTvShows();
    }

    @GetMapping("/top-rated")
    public List<TmdbTvShowDTO> topRatedTvShows() {
        return tvShowService.topRatedTvShows();
    }
}
