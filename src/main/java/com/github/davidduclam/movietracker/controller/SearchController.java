package com.github.davidduclam.movietracker.controller;

import com.github.davidduclam.movietracker.client.tmdb.dto.TmdbSearchResultDTO;
import com.github.davidduclam.movietracker.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/multi")
    public List<TmdbSearchResultDTO> searchMulti(@RequestParam String query) {
        return searchService.searchMulti(query);
    }

}
