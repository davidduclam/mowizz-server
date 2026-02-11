package com.github.davidduclam.movietracker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Long tmdbId;
    private String title;
    private LocalDate releaseDate;
    private String posterPath;
    private String backdropPath;
    private String overview;
}
