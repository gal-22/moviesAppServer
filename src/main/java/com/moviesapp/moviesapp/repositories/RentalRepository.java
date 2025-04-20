package com.moviesapp.moviesapp.repositories;

import com.moviesapp.moviesapp.models.Rental;
import com.moviesapp.moviesapp.models.User;
import com.moviesapp.moviesapp.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUser(User user);
    Optional<Rental> findFirstByUserAndMovieAndReturnDateIsNull(User user, Movie movie);
    List<Rental> findByMovieAndReturnDateIsNull(Movie movie);
}
