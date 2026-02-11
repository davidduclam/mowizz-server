package com.github.davidduclam.movietracker.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TmdbClientException.class)
    public ResponseEntity<ErrorResponse> handleExternal(TmdbClientException e) {
        HttpStatusCode status = e.getStatus() == null ? HttpStatus.SERVICE_UNAVAILABLE : e.getStatus();
        int code = status.value();
        String error = code >= 400 && code < 500
                ? "tmdb_client_error"
                : code >= 500 && code < 600 ? "tmdb_server_error" : "tmdb_unavailable";
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(error, e.getMessage()));
    }

    @ExceptionHandler(MediaAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleMediaAlreadyExists(MediaAlreadyExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("MEDIA_ALREADY_EXISTS", exception.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("USER_NOT_FOUND", exception.getMessage()));
    }
}
