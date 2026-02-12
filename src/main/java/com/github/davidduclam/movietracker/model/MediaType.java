package com.github.davidduclam.movietracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MediaType {
    TV, MOVIE;

    @JsonCreator
    public static MediaType fromJson(String value) {
        return MediaType.valueOf(value.trim().toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
