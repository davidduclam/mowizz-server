package com.github.davidduclam.movietracker.dto;

import com.github.davidduclam.movietracker.model.MediaType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddUserMediaRequestDTO {

    @NotNull(message = "tmdbId is required")
    private Long tmdbId;

    @NotNull(message = "mediaType is required")
    private MediaType mediaType;
}
