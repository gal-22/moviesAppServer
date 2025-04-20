package com.moviesapp.moviesapp.services;

import com.moviesapp.moviesapp.models.Rental;
import com.moviesapp.moviesapp.models.User;
import com.moviesapp.moviesapp.models.Movie;
import com.moviesapp.moviesapp.repositories.RentalRepository;
import com.moviesapp.moviesapp.repositories.UserRepository;
import com.moviesapp.moviesapp.repositories.MovieRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public RentalService(RentalRepository rentalRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    private User getAuthenticatedUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Rent a Movie
     */
    @Transactional
    public Map<String, String> rentMovie(Long movieId) {
        User user = getAuthenticatedUser();
        Optional<Movie> movie = movieRepository.findById(movieId);

        if (movie.isEmpty()) {
            return Map.of("message", "Error: Movie not found");
        }

        if (rentalRepository.findFirstByUserAndMovieAndReturnDateIsNull(user, movie.get()).isPresent()) {
            return Map.of("message", "Error: You have already rented this movie");
        }

        Rental rental = new Rental(user, movie.get());
        rentalRepository.save(rental);

        return Map.of("message", "Movie rented successfully");
    }

    /**
     * Return a Movie
     */
    @Transactional
    public Map<String, String> returnMovie(Long movieId) {
        User user = getAuthenticatedUser();
        Optional<Movie> movie = movieRepository.findById(movieId);

        if (movie.isEmpty()) {
            return Map.of("message", "Error: Movie not found");
        }

        Optional<Rental> rental = rentalRepository.findFirstByUserAndMovieAndReturnDateIsNull(user, movie.get());

        if (rental.isEmpty()) {
            return Map.of("message", "Error: You have not rented this movie");
        }

        rental.get().setReturnDate(LocalDateTime.now());
        rentalRepository.save(rental.get());

        return Map.of("message", "Movie returned successfully");
    }

    /**
     * Get Rental History for a User
     */
    public List<Rental> getUserRentalHistory() {
        User user = getAuthenticatedUser();
        return rentalRepository.findByUser(user);
    }
}
