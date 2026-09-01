package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.TmdbClient;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbImageResultsDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbTvShowDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbVideoDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbVideoResultsDTO;
import com.github.davidduclam.movietracker.dto.TrailerDTO;
import com.github.davidduclam.movietracker.dto.TvShowResponseDTO;
import com.github.davidduclam.movietracker.dto.UserMediaRequestDTO;
import com.github.davidduclam.movietracker.error.MediaNotFoundException;
import com.github.davidduclam.movietracker.model.MediaType;
import com.github.davidduclam.movietracker.model.TvShow;
import com.github.davidduclam.movietracker.repository.TvShowRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TvShowServiceTest {

    @Mock
    private TvShowRepository tvShowRepository;

    @Mock
    private TmdbClient tmdbClient;

    @InjectMocks
    private TvShowService tvShowService;

    @Test
    void saveTvShowToDb_tvShowNotInDb_savesTvShow() {
        TmdbVideoResultsDTO tmdbVideoResultsDTO = new TmdbVideoResultsDTO(List.of());
        TmdbTvShowDTO tmdbTvShowDTO = new TmdbTvShowDTO(1L,"", "", LocalDate.now(), "", "", 0.0, tmdbVideoResultsDTO, new TmdbImageResultsDTO(List.of()));

        UserMediaRequestDTO userMedia = new UserMediaRequestDTO(1L, MediaType.TV);
        when(tvShowRepository.findByTmdbId(1L)).thenReturn(Optional.empty());
        when(tmdbClient.fetchTvShowDetails(1L)).thenReturn(tmdbTvShowDTO);

        tvShowService.saveTvShowToDb(userMedia);

        verify(tvShowRepository).save(any(TvShow.class));
    }

    @Test
    void saveTvShowToDb_tvShowAlreadyInDb_doesNotSave() {
        UserMediaRequestDTO userMedia = new UserMediaRequestDTO(1L, MediaType.TV);
        when(tvShowRepository.findByTmdbId(1L)).thenReturn(Optional.of(new TvShow()));

        tvShowService.saveTvShowToDb(userMedia);

        verify(tvShowRepository, never()).save(any());
    }

    @Test
    void getTvShowFromDb_foundTvShow() {
        TvShow tvShow = new TvShow();
        tvShow.setTmdbId(1L);
        tvShow.setTitle("Andor");

        when(tvShowRepository.findByTmdbId(1L)).thenReturn(Optional.of(tvShow));

        TvShowResponseDTO result = tvShowService.getTvShowFromDb(1L);

        assertEquals(1L, result.id());
        assertEquals("Andor", result.name());
    }

    @Test
    void getTvShowFromDb_notFound_throwsException() {
        assertThrows(MediaNotFoundException.class, () -> tvShowService.getTvShowFromDb(1L));
    }

    @Test
    void testPopularTvShows() {
        List<TmdbTvShowDTO> list = new ArrayList<>();
        TmdbVideoResultsDTO videos = new TmdbVideoResultsDTO(List.of());
        TmdbTvShowDTO movie = new TmdbTvShowDTO(1L, "", "", LocalDate.now(), "", "", 0.0, videos, new TmdbImageResultsDTO(List.of()));
        list.add(movie);

        when(tmdbClient.popularTvShows()).thenReturn(list);

        List<TvShowResponseDTO> result = tvShowService.popularTvShows();

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
    }

    @Test

    void testTopRatedTvShows() {
        List<TmdbTvShowDTO> list = new ArrayList<>();
        TmdbVideoResultsDTO videos = new TmdbVideoResultsDTO(List.of());
        TmdbTvShowDTO movie = new TmdbTvShowDTO(1L, "", "", LocalDate.now(), "", "", 0.0, videos, new TmdbImageResultsDTO(List.of()));
        list.add(movie);

        when(tmdbClient.topRatedTvShows()).thenReturn(list);

        List<TvShowResponseDTO> result = tvShowService.topRatedTvShows();

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
    }

    @Test
    void getTvShowTrailer_foundTrailer() {
        List<TmdbVideoDTO> list = new ArrayList<>();
        TmdbVideoDTO video = new TmdbVideoDTO("Some Movie Trailer", true, "abc123", "YouTube", "Trailer");
        list.add(video);

        when(tmdbClient.fetchTvShowTrailers(1L)).thenReturn(list);

        TrailerDTO result = tvShowService.getTvShowTrailer(1L);

        assertEquals("Some Movie Trailer", result.name());
        assertEquals("abc123", result.key());
    }

    @Test
    void getTvShowTrailer_throwsException() {
        assertThrows(MediaNotFoundException.class, () -> tvShowService.getTvShowTrailer(1L));
    }
}
