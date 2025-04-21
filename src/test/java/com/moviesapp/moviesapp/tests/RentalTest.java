package com.moviesapp.moviesapp.tests;

import com.moviesapp.moviesapp.models.Rental;
import com.moviesapp.moviesapp.models.User;
import com.moviesapp.moviesapp.models.Movie;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RentalTest {

    @Test
    void isReturned_falseWhenReturnDateNull() {
        User user = new User();
        Movie movie = new Movie();
        Rental rental = new Rental(user, movie);

        // Newly created Rental.returnDate is null
        assertFalse(rental.isReturned(), "Rental with null returnDate should not be marked as returned");
    }

    @Test
    void isReturned_trueWhenReturnDateSet() {
        User user = new User();
        Movie movie = new Movie();
        Rental rental = new Rental(user, movie);

        LocalDateTime now = LocalDateTime.now();
        rental.setReturnDate(now);

        assertTrue(rental.isReturned(), "Rental with a non-null returnDate should be marked as returned");
        assertEquals(now, rental.getReturnDate(), "getReturnDate() should return exactly what was set");
    }
}
