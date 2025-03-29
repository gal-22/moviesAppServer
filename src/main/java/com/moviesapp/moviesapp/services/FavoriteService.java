package com.moviesapp.moviesapp.services;

import com.moviesapp.moviesapp.models.Movie;
import com.moviesapp.moviesapp.models.User;
import com.moviesapp.moviesapp.repositories.MovieRepository;
import com.moviesapp.moviesapp.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FavoriteService {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public FavoriteService(UserRepository userRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    private User getAuthenticatedUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * ✅ Add or remove a favorite movie
     */
    public Map<String, String> toggleFavorite(Long movieId) {
        User user = getAuthenticatedUser();
        Optional<Movie> movie = movieRepository.findById(movieId);

        if (movie.isEmpty()) {
            return Map.of("message", "Error: Movie not found");
        }

        Set<Movie> favorites = user.getFavoriteMovies();

        if (favorites.contains(movie.get())) {
            favorites.remove(movie.get());
            userRepository.save(user);
            return Map.of("message", "Movie removed from favorites");
        } else {
            favorites.add(movie.get());
            userRepository.save(user);
            return Map.of("message", "Movie added to favorites");
        }
    }

    /**
     * ✅ Get all favorite movies of the user
     */
    public List<Movie> getUserFavorites() {
        User user = getAuthenticatedUser();
        return new ArrayList<>(user.getFavoriteMovies());
    }
}
