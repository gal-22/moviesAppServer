package com.moviesapp.moviesapp.controllers;

import com.moviesapp.moviesapp.dto.MovieView;
import com.moviesapp.moviesapp.services.MovieFetchService;
import com.moviesapp.moviesapp.services.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    private final MovieFetchService movieFetchService;

    public MovieController(MovieService movieService, MovieFetchService movieFetchService) {
        this.movieService = movieService;
        this.movieFetchService = movieFetchService;
    }

    @GetMapping
    public List<MovieView> getMovies() {
        return movieService.getAllMovies();
    }

    /*
       Demo Function, used to fetch movies and populate the DB. These movies will represent the DVDs
     */
    @PostMapping("/refetch")
    public ResponseEntity<?> fetchMoviesFromTMDB() {
        Map<String, String> response = new HashMap<>();
        try {
            movieFetchService.fetchAndStoreMovies();
            response.put("message", "Movies fetched and updated successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to fetch movies: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}