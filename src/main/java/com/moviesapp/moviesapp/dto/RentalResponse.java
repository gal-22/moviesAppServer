package com.moviesapp.moviesapp.dto;

import com.moviesapp.moviesapp.models.MovieView;
import com.moviesapp.moviesapp.models.Rental;
import com.moviesapp.moviesapp.models.User;

import java.time.LocalDateTime;

public class RentalResponse {
    private Long id;
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;
    private boolean returned;
    private MovieView movie;

    public RentalResponse(Rental rental, User currentUser) {
        this.id         = rental.getId();
        this.rentalDate = rental.getRentalDate();
        this.returnDate = rental.getReturnDate();
        this.returned   = rental.isReturned();

        boolean isRented      = !rental.isReturned();
        boolean isRentedByUsr = rental.getUser().equals(currentUser);
        boolean isFav         = currentUser.getFavoriteMovies().contains(rental.getMovie());

        this.movie = new MovieView(
                rental.getMovie(),
                isRented,
                isRentedByUsr,
                isFav
        );
    }
    
    public Long getId()                { return id; }
    public LocalDateTime getRentalDate(){ return rentalDate; }
    public LocalDateTime getReturnDate(){ return returnDate; }
    public boolean isReturned()        { return returned; }
    public MovieView getMovie()        { return movie; }
}