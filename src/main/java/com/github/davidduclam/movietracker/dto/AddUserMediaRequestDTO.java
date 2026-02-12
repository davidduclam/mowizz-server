package com.github.davidduclam.movietracker.dto;

import com.github.davidduclam.movietracker.model.MediaType;
import lombok.Data;

@Data
public class AddUserMediaRequestDTO {

    private Long tmdbId;
    private MediaType mediaType;
}
