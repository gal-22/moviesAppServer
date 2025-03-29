package com.moviesapp.moviesapp.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Movie movie;

    private LocalDateTime rentalDate;
    private LocalDateTime returnDate; // Null means not returned

    public Rental() {}

    public Rental(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
        this.rentalDate = LocalDateTime.now();
        this.returnDate = null;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public Movie getMovie() { return movie; }
    public LocalDateTime getRentalDate() { return rentalDate; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }

    public boolean isReturned() {
        return returnDate != null;
    }
}
