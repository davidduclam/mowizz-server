package com.github.davidduclam.movietracker.dto;

public sealed interface WatchlistItemDTO permits MovieWatchlistItemDTO, TvShowWatchlistItemDTO {}
