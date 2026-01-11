package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.model.Movie;
import com.github.davidduclam.movietracker.model.User;
import com.github.davidduclam.movietracker.model.UserMovie;
import com.github.davidduclam.movietracker.repository.UserMovieRepository;
import org.springframework.stereotype.Service;

@Service
public class UserMovieService {
    private final UserMovieRepository userMovieRepository;

    public UserMovieService(UserMovieRepository userMovieRepository) {
        this.userMovieRepository = userMovieRepository;
    }
}
