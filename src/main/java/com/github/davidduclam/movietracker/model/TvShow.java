package com.github.davidduclam.movietracker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "tv_shows")
public class TvShow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tmdbId;
    private String title;
    private LocalDate firstAirDate;
    private String posterPath;
    private String backdropPath;
    private String overview;
}
