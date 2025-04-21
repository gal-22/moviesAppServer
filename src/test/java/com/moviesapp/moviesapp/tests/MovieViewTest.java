package com.moviesapp.moviesapp.tests;

import com.moviesapp.moviesapp.models.Movie;
import com.moviesapp.moviesapp.models.MovieView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieViewTest {

    @Test
    void defaultConstructor_flagsAreFalse() {
        MovieView mv = new MovieView();
        assertFalse(mv.isRented(), "default isRented should be false");
        assertFalse(mv.isRentedByUser(), "default isRentedByUser should be false");
        assertFalse(mv.isFavorite(), "default isFavorite should be false");
    }

    @Test
    void flagConstructor_setsFlagsCorrectly() {
        MovieView mv = new MovieView(true, false, true);
        assertTrue(mv.isRented(), "isRented");
        assertFalse(mv.isRentedByUser(), "isRentedByUser");
        assertTrue(mv.isFavorite(), "isFavorite");
    }

    @Test
    void fullConstructor_copiesMovieFieldsAndFlags() {
        // prepare an underlying Movie
        Movie m = new Movie();
        m.setId(123L);
        m.setTmdbId(999L);
        m.setTitle("Test Title");

        // wrap it
        MovieView mv = new MovieView(m, true, true, false);

        // inherited getters
        assertEquals(123L, mv.getId());
        assertEquals(999L, mv.getTmdbId());
        assertEquals("Test Title", mv.getTitle());

        // our flags
        assertTrue(mv.isRented());
        assertTrue(mv.isRentedByUser());
        assertFalse(mv.isFavorite());
    }
}
