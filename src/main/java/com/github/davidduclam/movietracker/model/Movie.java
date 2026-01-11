package com.github.davidduclam.movietracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue
    private Long Id;

    private Long tmdbId;
    private String title;
    private LocalDate releaseDate;
    private String posterPath;
    private String description;
}
