package com.moviesapp.moviesapp.controllers;

import com.moviesapp.moviesapp.dto.MovieView;
import com.moviesapp.moviesapp.models.Rental;
import com.moviesapp.moviesapp.repositories.RentalRepository;
import com.moviesapp.moviesapp.repositories.UserRepository;
import com.moviesapp.moviesapp.services.RentalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;// ← new

    public RentalController(RentalService rentalService,
                            UserRepository userRepository, RentalRepository rentalRepository) {
        this.rentalService = rentalService;
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
        return rentalService.getUserRentalHistoryMovies();
    }

}