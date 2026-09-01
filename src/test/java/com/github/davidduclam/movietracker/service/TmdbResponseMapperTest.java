package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbImageDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbImageResultsDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbVideoDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbVideoResultsDTO;
import com.github.davidduclam.movietracker.dto.TrailerDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TmdbResponseMapperTest {

    private static TmdbVideoDTO video(String name, boolean official, String key, String site, String type) {
        return new TmdbVideoDTO(name, official, key, site, type);
    }

    @Test
    void officialTrailerKey_returnsFirstOfficialYouTubeTrailer() {
        TmdbVideoResultsDTO videos = new TmdbVideoResultsDTO(List.of(
                video("Teaser", true, "teaser1", "YouTube", "Teaser"),
                video("Official Trailer", true, "trailer1", "YouTube", "Trailer"),
                video("Another Trailer", true, "trailer2", "YouTube", "Trailer")
        ));

        assertEquals("trailer1", TmdbResponseMapper.officialTrailerKey(videos));
    }

    @Test
    void officialTrailerKey_nullVideos_returnsNull() {
        assertNull(TmdbResponseMapper.officialTrailerKey(null));
    }

    @Test
    void officialTrailerKey_nullResults_returnsNull() {
        assertNull(TmdbResponseMapper.officialTrailerKey(new TmdbVideoResultsDTO(null)));
    }

    @Test
    void officialTrailerKey_noMatch_returnsNull() {
        TmdbVideoResultsDTO videos = new TmdbVideoResultsDTO(List.of(
                video("Unofficial Trailer", false, "a", "YouTube", "Trailer"),
                video("Vimeo Trailer", true, "b", "Vimeo", "Trailer"),
                video("Featurette", true, "c", "YouTube", "Featurette")
        ));

        assertNull(TmdbResponseMapper.officialTrailerKey(videos));
    }

    @Test
    void firstLogoPath_returnsFirstFilePath() {
        TmdbImageResultsDTO images = new TmdbImageResultsDTO(List.of(
                new TmdbImageDTO("/logo1.png"),
                new TmdbImageDTO("/logo2.png")
        ));

        assertEquals("/logo1.png", TmdbResponseMapper.firstLogoPath(images));
    }

    @Test
    void firstLogoPath_nullImages_returnsNull() {
        assertNull(TmdbResponseMapper.firstLogoPath(null));
    }

    @Test
    void firstLogoPath_nullLogos_returnsNull() {
        assertNull(TmdbResponseMapper.firstLogoPath(new TmdbImageResultsDTO(null)));
    }

    @Test
    void firstLogoPath_emptyLogos_returnsNull() {
        assertNull(TmdbResponseMapper.firstLogoPath(new TmdbImageResultsDTO(new ArrayList<>())));
    }

    @Test
    void toTrailer_mapsKeyNameAndSite() {
        TrailerDTO result = TmdbResponseMapper.toTrailer(video("Official Trailer", true, "abc123", "YouTube", "Trailer"));

        assertEquals("abc123", result.key());
        assertEquals("Official Trailer", result.name());
        assertEquals("YouTube", result.site());
    }
}
