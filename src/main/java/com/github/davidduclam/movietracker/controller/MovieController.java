package com.github.davidduclam.movietracker.controller;

import com.github.davidduclam.movietracker.dto.MovieResponseDTO;
import com.github.davidduclam.movietracker.dto.TrailerDTO;
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

    @GetMapping("/{tmdbId}")
    public MovieResponseDTO fetchMovieDetails(@PathVariable Long tmdbId) {
        return movieService.fetchMovieDetails(tmdbId);
    }

    @GetMapping("/{tmdbId}/trailer")
    public TrailerDTO fetchTrailer(@PathVariable Long tmdbId) {
        return movieService.getMovieTrailer(tmdbId);
    }

    @GetMapping("/popular")
    public List<MovieResponseDTO> popularMovies() {
        return movieService.popularMovies();
    }

    @GetMapping("/top-rated")
    public List<MovieResponseDTO> topRatedMovies() {
        return movieService.topRatedMovies();
    }

    @GetMapping("/upcoming")
    public List<MovieResponseDTO> upcomingMovies() {
        return movieService.upcomingMovies();
    }
}
