package com.github.davidduclam.movietracker.controller;

import com.github.davidduclam.movietracker.model.UserMedia;
import com.github.davidduclam.movietracker.service.UserMediaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserMediaController {
    private final UserMediaService userMediaService;

    public UserMediaController(UserMediaService userMediaService) {
        this.userMediaService = userMediaService;
    }

    @PostMapping("/users/{user_id}/movie")
    public UserMedia saveMovieToDb(@PathVariable Long user_id, @RequestBody Long tmdbId) {
        userMediaService.saveMovieToDb(tmdbId);
        return userMediaService.saveUserMediaMovieToDb(user_id, tmdbId);

    }

    @PostMapping("/users/{user_id}/tvshow")
    public UserMedia saveTvShowToDb(@PathVariable Long user_id, @RequestBody Long tmdbId) {
        userMediaService.saveTvShowToDb(tmdbId);
        return userMediaService.saveUserMediaTvShowToDb(user_id, tmdbId);

    }
}
