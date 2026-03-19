package com.github.davidduclam.movietracker.controller;

import com.github.davidduclam.movietracker.dto.UserMediaRequestDTO;
import com.github.davidduclam.movietracker.dto.WatchlistItemDTO;
import com.github.davidduclam.movietracker.model.UserMedia;
import com.github.davidduclam.movietracker.service.UserMediaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserMediaController {
    private final UserMediaService userMediaService;

    public UserMediaController(UserMediaService userMediaService) {
        this.userMediaService = userMediaService;
    }

    @PostMapping("/users/{user_id}/media")
    public UserMedia saveMediaToDb(@PathVariable Long user_id, @Valid @RequestBody UserMediaRequestDTO userMediaRequestDTO) {
        return userMediaService.addMediaToUser(user_id, userMediaRequestDTO);
    }

    @GetMapping("/users/{user_id}/media")
    public List<WatchlistItemDTO> getMediaFromUser(@PathVariable Long user_id) {
        return userMediaService.getMediaFromUser(user_id);
    }

    @DeleteMapping("/users/{user_id}/media")
    public void deleteMediaFromUser(@PathVariable Long user_id, @Valid @RequestBody UserMediaRequestDTO userMediaRequestDTO) {
        userMediaService.deleteMediaFromUser(user_id, userMediaRequestDTO);
    }
}
