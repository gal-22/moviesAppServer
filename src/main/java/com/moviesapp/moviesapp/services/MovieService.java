package com.moviesapp.moviesapp.services;

import com.moviesapp.moviesapp.models.Movie;
import com.moviesapp.moviesapp.dto.MovieView;
import com.moviesapp.moviesapp.models.User;
import com.moviesapp.moviesapp.repositories.MovieRepository;
import com.moviesapp.moviesapp.repositories.RentalRepository;
import com.moviesapp.moviesapp.repositories.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    public MovieService(MovieRepository movieRepository, RentalRepository rentalRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public List<MovieView> getAllMovies() {
        User user = getAuthenticatedUser();
        List<Movie> movies = movieRepository.findAll();
        List<MovieView> movieList = new ArrayList<>();

        for (Movie movie : movies) {
            boolean isRentedByAnyone = !rentalRepository.findByMovieAndReturnDateIsNull(movie).isEmpty();
            boolean isRentedByUser = rentalRepository.findFirstByUserAndMovieAndReturnDateIsNull(user, movie).isPresent(); // Fetched rentals of the user without a return date
            boolean isFavorite = user.getFavoriteMovies().contains(movie);

            MovieView movieData = new MovieView(
                movie,
                isRentedByAnyone,
                isRentedByUser,
                isFavorite
            );

            movieList.add(movieData);
        }

        return movieList;
    }
}