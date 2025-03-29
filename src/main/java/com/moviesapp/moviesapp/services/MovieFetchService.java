package com.moviesapp.moviesapp.services;

import com.moviesapp.moviesapp.models.Movie;
import com.moviesapp.moviesapp.repositories.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MovieFetchService {
    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate;
    private final String API_KEY = "ba50009df309cfd8d537ba914557af7f";
    private final String BASE_URL = "https://api.themoviedb.org/3/movie/now_playing";

    public MovieFetchService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        this.restTemplate = new RestTemplate();
    }

    @Transactional
    public void fetchAndStoreMovies() {
        try {
            List<Movie> newMovies = new ArrayList<>();
            int totalMoviesFetched = 0;
            int page = 1;

            // Keep track of TMDb IDs we've already processed to avoid duplicates
            Set<Long> processedTmdbIds = new HashSet<>();

            while (totalMoviesFetched < 100) {
                String url = BASE_URL + "?api_key=" + API_KEY + "&language=en-US&page=" + page;

                System.out.println("Fetching page " + page + " from TMDB API");

                ResponseEntity<String> response = restTemplate.exchange(
                        url, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class
                );

                JSONObject jsonResponse = new JSONObject(response.getBody());
                JSONArray results = jsonResponse.getJSONArray("results");

                System.out.println("Received " + results.length() + " movies from TMDB API");

                if (results.length() == 0) {
                    break; // No more results to process
                }

                for (int i = 0; i < results.length() && totalMoviesFetched < 100; i++) {
                    JSONObject movieJson = results.getJSONObject(i);

                    Long tmdbId = movieJson.getLong("id");

                    // Skip if we've already processed this ID or if it already exists in the database
                    if (processedTmdbIds.contains(tmdbId) || movieRepository.existsByTmdbId(tmdbId)) {
                        System.out.println("Skipping duplicate TMDB ID: " + tmdbId);
                        continue;
                    }

                    processedTmdbIds.add(tmdbId);
                    String title = movieJson.getString("title");
                    String originalTitle = movieJson.optString("original_title", "");
                    String overview = movieJson.optString("overview", "");
                    String releaseDate = movieJson.optString("release_date", "");
                    String originalLanguage = movieJson.optString("original_language", "");
                    String posterPath = movieJson.optString("poster_path", "");
                    String backdropPath = movieJson.optString("backdrop_path", "");

                    boolean adult = movieJson.optBoolean("adult", false);
                    boolean video = movieJson.optBoolean("video", false);

                    double voteAverage = movieJson.optDouble("vote_average", 0.0);
                    int voteCount = movieJson.optInt("vote_count", 0);
                    double popularity = movieJson.optDouble("popularity", 0.0);

                    // Parse genre_ids array - IMPORTANT PART FOR GENRES
                    List<Integer> genreIds = new ArrayList<>();

                    // Check if genre_ids exists in the JSON
                    if (movieJson.has("genre_ids")) {
                        JSONArray genreArray = movieJson.getJSONArray("genre_ids");
                        System.out.println("Movie: " + title + " - Found " + genreArray.length() + " genres");

                        for (int j = 0; j < genreArray.length(); j++) {
                            genreIds.add(genreArray.getInt(j));
                        }

                        // Print the genre IDs we found
                        System.out.println("Genre IDs for " + title + ": " + genreIds);
                    } else {
                        System.out.println("WARNING: No genre_ids found for movie: " + title);
                    }

                    Movie movie = new Movie();
                    movie.setTmdbId(tmdbId);
                    movie.setTitle(title);
                    movie.setOriginalTitle(originalTitle);
                    movie.setOverview(overview);
                    movie.setReleaseDate(releaseDate);
                    movie.setOriginalLanguage(originalLanguage);
                    movie.setPosterPath(posterPath);
                    movie.setBackdropPath(backdropPath);
                    movie.setAdult(adult);
                    movie.setVideo(video);
                    movie.setVoteAverage(voteAverage);
                    movie.setVoteCount(voteCount);
                    movie.setPopularity(popularity);

                    movie.setGenreIds(genreIds);

                    newMovies.add(movie);
                    totalMoviesFetched++;
                }

                page++; // Move to the next page of TMDB results
            }

            System.out.println("Saving " + newMovies.size() + " new movies to database");

            // Save all movies
            if (!newMovies.isEmpty()) {
                movieRepository.saveAll(newMovies);

                // Verify genre IDs were saved by checking a few movies
                if (!newMovies.isEmpty()) {
                    Movie firstMovie = newMovies.get(0);
                    Long firstMovieId = firstMovie.getTmdbId();
                    System.out.println("First movie saved: " + firstMovie.getTitle());
                    System.out.println("Genre IDs: " + firstMovie.getGenreIds());

                    // Try to load it back to verify
                    Movie savedMovie = movieRepository.findByTmdbId(firstMovieId).orElse(null);
                    if (savedMovie != null) {
                        System.out.println("Loaded back from DB - Genre IDs: " + savedMovie.getGenreIds());
                    }
                }
            } else {
                System.out.println("No new movies to save");
            }
        } catch (Exception e) {
            System.err.println("Error fetching and storing movies: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch movies: " + e.getMessage(), e);
        }
    }
}