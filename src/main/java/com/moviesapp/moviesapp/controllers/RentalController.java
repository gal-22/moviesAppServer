package com.moviesapp.moviesapp.controllers;

import com.moviesapp.moviesapp.dto.RentalResponse;
import com.moviesapp.moviesapp.models.Movie;
import com.moviesapp.moviesapp.models.MovieView;
import com.moviesapp.moviesapp.models.Rental;
import com.moviesapp.moviesapp.models.User;
import com.moviesapp.moviesapp.repositories.RentalRepository;
import com.moviesapp.moviesapp.repositories.UserRepository;
import com.moviesapp.moviesapp.services.RentalService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;  // ← new

    public RentalController(RentalService rentalService,
                            UserRepository userRepository, RentalRepository rentalRepository) {
        this.rentalService = rentalService;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
    }

    /**
     * Rent a Movie
     */
    @PostMapping("/rent/{movieId}")
    public Map<String, String> rentMovie(@PathVariable Long movieId) {
        return rentalService.rentMovie(movieId);
    }

    /**
     * Return a Movie
     */
    @PostMapping("/return/{movieId}")
    public Map<String, String> returnMovie(@PathVariable Long movieId) {
        return rentalService.returnMovie(movieId);
    }

    /**
     * Raw Rental entities
     */
    @GetMapping("/history")
    public List<Rental> getRentalHistory() {
        return rentalService.getUserRentalHistory();
    }


    /**
     * Only the user's currently active rented movies,
     * returned as MovieView so the client can reuse its Movie UI.
     */
    @GetMapping("/history/movies")
    public List<MovieView> getRentalHistoryMovies() {
        User currentUser = getAuthenticatedUser();

        return rentalService.getUserRentalHistory().stream()
                .map(rental -> {
                    // the movie on this rental record
                    Movie movie = rental.getMovie();

                    // 1) is it currently rented by *anyone*?
                    boolean isRentedByAnyone =
                            !rentalRepository
                                    .findByMovieAndReturnDateIsNull(movie)
                                    .isEmpty();

                    // is it your own active rental?
                    boolean isRentedByUser =
                            rentalRepository
                                    .findFirstByUserAndMovieAndReturnDateIsNull(currentUser, movie)
                                    .isPresent();

                    // is it in your favorites?
                    boolean isFav =
                            currentUser.getFavoriteMovies().contains(movie);

                    return new MovieView(
                            movie,
                            isRentedByAnyone,
                            isRentedByUser,
                            isFav
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * Helper: look up the currently authenticated User entity.
     */
    private User getAuthenticatedUser() {
        UserDetails springUser = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String username = springUser.getUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}