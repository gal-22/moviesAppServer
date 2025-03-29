package com.moviesapp.moviesapp.controllers;

import com.moviesapp.moviesapp.models.Movie;
import com.moviesapp.moviesapp.services.FavoriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    /**
     * ✅ Toggle favorite status of a movie
     */
    @PostMapping("/{movieId}")
    public Map<String, String> toggleFavorite(@PathVariable Long movieId) {
        return favoriteService.toggleFavorite(movieId);
    }

    /**
     * ✅ Get all favorite movies of the user
     */
    @GetMapping
    public List<Movie> getFavorites() {
        return favoriteService.getUserFavorites();
    }
}
