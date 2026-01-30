package com.github.davidduclam.movietracker.error;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class TmdbClientException extends RuntimeException {

    private final HttpStatusCode status;

    public TmdbClientException(String message) {
        this(message, null, null);
    }

    public TmdbClientException(String message, Throwable cause) {
        this(message, null, cause);
    }

    public TmdbClientException(String message, HttpStatusCode status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

}
