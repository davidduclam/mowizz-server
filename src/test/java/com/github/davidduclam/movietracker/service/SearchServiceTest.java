package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.TmdbClient;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbIgnoredSearchResultDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbMovieResultDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbTvShowResultDTO;
import com.github.davidduclam.movietracker.dto.MovieSearchResultDTO;
import com.github.davidduclam.movietracker.dto.SearchResultDTO;
import com.github.davidduclam.movietracker.dto.TvShowSearchResultDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private TmdbClient tmdbClient;

    @InjectMocks
    private SearchService searchService;

    @Test
    void searchMulti_MovieResult() {
        TmdbMovieResultDTO tmdbMovie = new TmdbMovieResultDTO(
                1L, "movie", "/poster.jpg", 7.5, "Inception", "2010-07-16"
        );
        when(tmdbClient.searchMulti("inception")).thenReturn(List.of(tmdbMovie));

        List<SearchResultDTO> results = searchService.searchMulti("inception");

        assertEquals(1, results.size());
        MovieSearchResultDTO movie = (MovieSearchResultDTO) results.getFirst();
        assertEquals(1L, movie.id());
        assertEquals("movie", movie.mediaType());
        assertEquals("Inception", movie.title());
        assertEquals(LocalDate.of(2010, 7, 16), movie.releaseDate());
        assertEquals("/poster.jpg", movie.posterPath());
        assertEquals(7.5, movie.voteAverage());
    }

    @Test
    void searchMulti_TvShowResult() {
        TmdbTvShowResultDTO tmdbTvShow = new TmdbTvShowResultDTO(
                1L, "tv", "/poster.jpg", 7.5, "Stranger Things", "2010-07-16"
        );
        when(tmdbClient.searchMulti("stranger things")).thenReturn(List.of(tmdbTvShow));

        List<SearchResultDTO> results = searchService.searchMulti("stranger things");

        assertEquals(1, results.size());
        TvShowSearchResultDTO tvShow = (TvShowSearchResultDTO) results.getFirst();
        assertEquals(1L, tvShow.id());
        assertEquals("tv", tvShow.mediaType());
        assertEquals("Stranger Things", tvShow.name());
        assertEquals(LocalDate.of(2010, 7, 16), tvShow.firstAirDate());
        assertEquals("/poster.jpg", tvShow.posterPath());
        assertEquals(7.5, tvShow.voteAverage());
    }

    @Test
    void searchMulti_MovieNullReleaseDate() {
        TmdbMovieResultDTO tmdbMovie = new TmdbMovieResultDTO(
                1L, "movie", "/poster.jpg", 7.5, "Inception", ""
        );
        when(tmdbClient.searchMulti("inception")).thenReturn(List.of(tmdbMovie));

        List<SearchResultDTO> results = searchService.searchMulti("inception");
        MovieSearchResultDTO movie = (MovieSearchResultDTO) results.getFirst();
        assertNull(movie.releaseDate());
    }

    @Test
    void searchMulti_TvShowNullReleaseDate() {
        TmdbTvShowResultDTO tmdbTvShow = new TmdbTvShowResultDTO(
                1L, "movie", "/poster.jpg", 7.5, "Stranger Things", ""
        );
        when(tmdbClient.searchMulti("stranger things")).thenReturn(List.of(tmdbTvShow));

        List<SearchResultDTO> results = searchService.searchMulti("stranger things");
        TvShowSearchResultDTO tvShow = (TvShowSearchResultDTO) results.getFirst();
        assertNull(tvShow.firstAirDate());
    }

    @Test
    void searchMulti_IgnoredType() {
        TmdbIgnoredSearchResultDTO ignored = new TmdbIgnoredSearchResultDTO(1L, "");
        when(tmdbClient.searchMulti("test")).thenReturn(List.of(ignored));

        assertThrows(IllegalStateException.class, () -> searchService.searchMulti("test"));
    }

    @Test
    void searchMulti_EmptyList() {
        when(tmdbClient.searchMulti("")).thenReturn(List.of());

        List<SearchResultDTO> results = searchService.searchMulti("");

        assertEquals(0, results.size());
        assertEquals(results, List.of());
    }
}
