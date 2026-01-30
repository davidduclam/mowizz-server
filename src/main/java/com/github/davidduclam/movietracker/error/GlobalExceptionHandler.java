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
}
