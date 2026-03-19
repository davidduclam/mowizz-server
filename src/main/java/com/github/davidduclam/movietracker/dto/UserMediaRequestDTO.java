package com.github.davidduclam.movietracker.dto;

import com.github.davidduclam.movietracker.model.MediaType;
import jakarta.validation.constraints.NotNull;

public record UserMediaRequestDTO(
    @NotNull(message = "tmdbId is required")
    Long tmdbId,

    @NotNull(message = "mediaType is required")
    MediaType mediaType
) {}
