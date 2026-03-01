package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.TmdbClient;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbIgnoredSearchResultDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbMovieResultDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbSearchResultDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbTvShowResultDTO;
import com.github.davidduclam.movietracker.dto.MovieSearchResultDTO;
import com.github.davidduclam.movietracker.dto.SearchResultDTO;
import com.github.davidduclam.movietracker.dto.TvShowSearchResultDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SearchService {
    private final TmdbClient tmdbClient;

    public SearchService(TmdbClient tmdbClient) {
        this.tmdbClient = tmdbClient;
    }

    /**
     * Performs a multi-search query for movies and TV shows using the provided search string.
     *
     * @param query The search query string used to find matching movies or TV shows.
     * @return A list of {@code SearchResultDTO} objects, which contains either movie or TV show search results.
     */
    public List<SearchResultDTO> searchMulti(String query) {
        List<TmdbSearchResultDTO> tmdbSearchResultDTOList = tmdbClient.searchMulti(query);
        return tmdbSearchResultDTOList.stream().map(this::toSearchResult).toList();
    }

    /**
     * Converts a {@link TmdbSearchResultDTO} instance into a corresponding {@link SearchResultDTO}.
     *
     * @param result The {@link TmdbSearchResultDTO} instance to convert. It can be one of the following types:
     *               {@link TmdbMovieResultDTO} for movies, {@link TmdbTvShowResultDTO} for TV shows,
     *               or {@link TmdbIgnoredSearchResultDTO} for unsupported types.
     * @return A {@link SearchResultDTO} instance. The returned object is a {@link MovieSearchResultDTO} if the input is
     *         a {@link TmdbMovieResultDTO}, or a {@link TvShowSearchResultDTO} if the input is a {@link TmdbTvShowResultDTO}.
     * @throws IllegalStateException If the input is an unsupported type, such as {@link TmdbIgnoredSearchResultDTO}.
     */
    private SearchResultDTO toSearchResult(TmdbSearchResultDTO result) {
        return switch (result) {
            case TmdbMovieResultDTO m -> toMovieSearchResult(m);
            case TmdbTvShowResultDTO t -> toTvShowSearchResult(t);
            case TmdbIgnoredSearchResultDTO ignored -> throw new IllegalStateException("Unexpected search result type");
        };
    }

    /**
     * Converts a {@link TmdbMovieResultDTO} instance into a {@link MovieSearchResultDTO} instance.
     *
     * @param m The {@link TmdbMovieResultDTO} instance representing the movie details retrieved from the TMDb API.
     *          This includes the movie's ID, media type, title, release date (as a string), poster path, and vote average.
     * @return A {@link MovieSearchResultDTO} instance that contains the movie's ID, media type,
     *         title, release date (as a {@link LocalDate}), poster path, and vote average.
     *         The release date will be parsed as a {@link LocalDate}, or set to {@code null} if it is blank or not provided.
     */
    private MovieSearchResultDTO toMovieSearchResult(TmdbMovieResultDTO m) {
        return new MovieSearchResultDTO(
                m.id(),
                m.mediaType(),
                m.title(),
                m.releaseDate() != null && !m.releaseDate().isBlank() ? LocalDate.parse(m.releaseDate()) : null,
                m.posterPath(),
                m.voteAverage());
    }

    /**
     * Converts a {@link TmdbTvShowResultDTO} instance into a {@link TvShowSearchResultDTO} instance.
     *
     * @param t The {@link TmdbTvShowResultDTO} instance representing the TV show details retrieved from the TMDb API.
     *          This includes the show's ID, media type, name, release date (as a string), poster path, and vote average.
     * @return A {@link TvShowSearchResultDTO} instance that contains the TV show's ID, media type, name,
     *         release date (as a {@link LocalDate}), poster path, and vote average.
     *         The release date will be parsed as a {@link LocalDate}, or set to {@code null} if it is blank or not provided.
     */
    private TvShowSearchResultDTO toTvShowSearchResult(TmdbTvShowResultDTO t) {
        return new TvShowSearchResultDTO(
                t.id(),
                t.mediaType(),
                t.name(),
                t.releaseDate() != null && !t.releaseDate().isBlank() ? LocalDate.parse(t.releaseDate()) : null,
                t.posterPath(),
                t.voteAverage());
    }
}