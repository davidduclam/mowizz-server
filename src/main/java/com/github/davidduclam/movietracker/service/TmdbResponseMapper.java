package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbImageDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbImageResultsDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbVideoDTO;
import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbVideoResultsDTO;
import com.github.davidduclam.movietracker.dto.TrailerDTO;

final class TmdbResponseMapper {
    private static final String TRAILER = "Trailer";
    private static final String YOUTUBE = "YouTube";

    private TmdbResponseMapper() {}

    /**
     * Extracts the key of the official trailer from a list of video results.
     * The method filters the video results to find a video that is marked as:
     * - Type: "Trailer"
     * - Official: true
     * - Site: "YouTube"
     * If multiple videos match, the key of the first matching video is returned.
     * If no matching video is found or the input is null, it returns null.
     *
     * @param videos the data transfer object containing the list of video results,
     *               which can include trailers, teasers, and other related videos
     * @return the key of the official trailer if found; otherwise, returns null
     */
    static String officialTrailerKey(TmdbVideoResultsDTO videos) {
        if (videos == null || videos.results() == null) return null;
        return videos.results().stream()
                .filter(v -> TRAILER.equals(v.type())
                        && Boolean.TRUE.equals(v.official())
                        && YOUTUBE.equals(v.site()))
                .map(TmdbVideoDTO::key)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the file path of the first logo from a collection of image results.
     * If the provided image results or the list of logos is null, the method returns null.
     *
     * @param images the data transfer object containing a list of logo image details
     * @return the file path of the first logo if available; otherwise, returns null
     */
    static String firstLogoPath(TmdbImageResultsDTO images) {
        if (images == null || images.logos() == null) return null;
        return images.logos().stream()
                .map(TmdbImageDTO::file_path)
                .findFirst()
                .orElse(null);
    }

    /**
     * Converts a {@link TmdbVideoDTO} to a {@link TrailerDTO}.
     *
     * @param tmdbVideoDTO the data transfer object containing video details, such as the key,
     *                     name, and site of the video
     * @return a {@link TrailerDTO} containing the key, name, and site of the trailer
     */
    static TrailerDTO toTrailer(TmdbVideoDTO tmdbVideoDTO) {
        return new TrailerDTO(
                tmdbVideoDTO.key(),
                tmdbVideoDTO.name(),
                tmdbVideoDTO.site()
        );
    }
}
