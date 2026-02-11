package com.github.davidduclam.movietracker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "user_media")
public class UserMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long tmdbId;
    private boolean watched = false;
    private Double personalRating;
    private LocalDate watchDate;
    private String mediaType;
}
