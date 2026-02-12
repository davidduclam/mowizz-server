package com.github.davidduclam.movietracker.error;

public class MediaAlreadyExistsException extends RuntimeException {
    public MediaAlreadyExistsException(String message) {
        super(message);
    }
}
