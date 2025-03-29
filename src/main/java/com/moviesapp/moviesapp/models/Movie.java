package com.moviesapp.moviesapp.models;

import com.moviesapp.moviesapp.utils.GenreListConverter;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long tmdbId; // TMDB Movie ID

    private String title;
    private String originalTitle;

    @Column(columnDefinition = "LONGTEXT")
    private String overview;
    private String releaseDate;
    private String originalLanguage;

    private String posterPath;
    private String backdropPath;

    private boolean adult;
    private boolean video;

    private double voteAverage;
    private int voteCount;
    private double popularity;

    // Using the converter to handle JSON serialization/deserialization
    @Convert(converter = GenreListConverter.class)
    @Column(name = "genre_ids", length = 255)
    private List<Integer> genreIds = new ArrayList<>();

    public Movie() {}

    public Movie(Movie other) {
        this.id = other.id;
        this.tmdbId = other.tmdbId;
        this.title = other.title;
        this.originalTitle = other.originalTitle;
        this.overview = other.overview;
        this.releaseDate = other.releaseDate;
        this.originalLanguage = other.originalLanguage;
        this.posterPath = other.posterPath;
        this.backdropPath = other.backdropPath;
        this.adult = other.adult;
        this.video = other.video;
        this.voteAverage = other.voteAverage;
        this.voteCount = other.voteCount;
        this.popularity = other.popularity;
        if (other.genreIds != null) {
            this.genreIds = new ArrayList<>(other.genreIds);
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTmdbId() { return tmdbId; }
    public void setTmdbId(Long tmdbId) { this.tmdbId = tmdbId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getOriginalTitle() { return originalTitle; }
    public void setOriginalTitle(String originalTitle) { this.originalTitle = originalTitle; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getOriginalLanguage() { return originalLanguage; }
    public void setOriginalLanguage(String originalLanguage) { this.originalLanguage = originalLanguage; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getBackdropPath() { return backdropPath; }
    public void setBackdropPath(String backdropPath) { this.backdropPath = backdropPath; }

    public boolean isAdult() { return adult; }
    public void setAdult(boolean adult) { this.adult = adult; }

    public boolean isVideo() { return video; }
    public void setVideo(boolean video) { this.video = video; }

    public double getVoteAverage() { return voteAverage; }
    public void setVoteAverage(double voteAverage) { this.voteAverage = voteAverage; }

    public int getVoteCount() { return voteCount; }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }

    public double getPopularity() { return popularity; }
    public void setPopularity(double popularity) { this.popularity = popularity; }

    public List<Integer> getGenreIds() {
        if (genreIds == null) {
            genreIds = new ArrayList<>();
        }
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        if (genreIds == null) {
            this.genreIds = new ArrayList<>();
        } else {
            this.genreIds = genreIds;
        }
    }
}