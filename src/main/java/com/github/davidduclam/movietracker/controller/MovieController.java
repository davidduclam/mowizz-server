package com.github.davidduclam.movietracker.controller;

import com.github.davidduclam.movietracker.client.tmdb.TmdbClient;
import com.github.davidduclam.movietracker.dto.TmdbMovieDTO;
import com.github.davidduclam.movietracker.model.Movie;
import com.github.davidduclam.movietracker.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;
    private final TmdbClient tmdbClient;

    public MovieController(MovieService movieService, TmdbClient tmdbClient) {
        this.movieService = movieService;
        this.tmdbClient = tmdbClient;
    }

    @GetMapping("/search")
    public List<TmdbMovieDTO> search(@RequestParam String query) {
        return tmdbClient.searchMovies(query);
    }

    @GetMapping("/{tmdbId}")
    public Movie saveMovie(@PathVariable Long tmdbId) {
        return movieService.getOrCreateMovie(tmdbId);
//        Optional<Movie> m = movieService.findByTmdbId(tmdbId);
//
//        try {
//            Movie movie = m.orElseThrow(() -> new NullPointerException("Not"));
//            return movieService.getOrCreateMovie(movie);
//        } catch (NullPointerException e) {
//            System.out.println(e.getMessage());
//            return null;
//        }
    }

//    @GetMapping("/{tmdbId}")
//    public String fetch(@PathVariable Long tmdbId) {
//        return tmdbClient.fetchMovieDetails(tmdbId);
//    }


}
