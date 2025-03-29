package com.moviesapp.moviesapp.repositories;

import com.moviesapp.moviesapp.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByTmdbId(Long tmdbId); // Prevent duplicates
    Optional<Movie> findByTmdbId(Long tmdbId); // Find by TMDB ID
}