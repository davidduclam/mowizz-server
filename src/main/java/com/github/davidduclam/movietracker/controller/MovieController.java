package com.github.davidduclam.movietracker.controller;

import com.github.davidduclam.movietracker.dto.TmdbMovieDTO;
import com.github.davidduclam.movietracker.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/search")
    public List<TmdbMovieDTO> search(@RequestParam String query) {
        return movieService.searchMovie(query);
    }

    @GetMapping("/{tmdbId}")
    public TmdbMovieDTO fetchMovieDetails(@PathVariable Long tmdbId) {
        return movieService.fetchMovieDetails(tmdbId);
    }

    @GetMapping("/popular")
    public List<TmdbMovieDTO> popularMovies() {
        return movieService.popularMovies();
    }

    @GetMapping("/top-rated")
    public List<TmdbMovieDTO> topRatedMovies() {
        return movieService.topRatedMovies();
    }

    @GetMapping("/upcoming")
    public List<TmdbMovieDTO> upcomingMovies() {
        return movieService.upcomingMovies();
    }
}
