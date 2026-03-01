package com.github.davidduclam.movietracker.controller;

import com.github.davidduclam.movietracker.dto.AddUserMediaRequestDTO;
import com.github.davidduclam.movietracker.model.UserMedia;
import com.github.davidduclam.movietracker.service.UserMediaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserMediaController {
    private final UserMediaService userMediaService;

    public UserMediaController(UserMediaService userMediaService) {
        this.userMediaService = userMediaService;
    }

    @PostMapping("/users/{user_id}/media")
    public UserMedia saveMediaToDb(@PathVariable Long user_id, @Valid @RequestBody AddUserMediaRequestDTO addUserMediaRequestDTO) {
        return userMediaService.addMediaToUser(user_id, addUserMediaRequestDTO);
    }
}
