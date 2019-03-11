package com.example.admin.themovieapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface theInterface
{
    @GET("movie/popular")
    Call<AllMovie> getMostPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<AllMovie> getTopRatedMovies(@Query("api_key") String apiKey);

//    This is for getting the movie trailer
    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMoviesTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);

//    Getting the movies review
       @GET("movie/{movie_id}/reviews")
       Call<AllReviews> getMoviesReviews(@Path("movie_id") int id, @Query("api_key") String apiKey);
}
