package com.moviesapp.moviesapp.models;

public class MovieView extends Movie {
    private boolean isRented;
    private boolean isRentedByUser;
    private boolean isFavorite;

    public MovieView() {
        super();
    }

    public MovieView(boolean isRented, boolean isRentedByUser, boolean isFavorite) {
        super();
        this.isRented = isRented;
        this.isRentedByUser = isRentedByUser;
        this.isFavorite = isFavorite;
    }

    public MovieView(Movie movie, boolean isRented, boolean isRentedByUser, boolean isFavorite) {
        super(movie);
        this.isRented = isRented;
        this.isRentedByUser = isRentedByUser;
        this.isFavorite = isFavorite;
    }

    // Getters
    public boolean isRented() {
        return isRented;
    }
    public boolean isRentedByUser() {
        return isRentedByUser;
    }
    public boolean isFavorite() {
        return isFavorite;
    }
}

