package com.example.admin.themovieapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDao
{
    @Insert
    void insertMovie(Movie movieObject);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<Movie> movies);

    @Delete
    void deleteMovie(Movie movieObject);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movieObject);

    @Query("SELECT * FROM Movies")
    LiveData<List<Movie>> loadMovies();

    @Delete
    void deleteMovies(List<Movie> movies);
}
