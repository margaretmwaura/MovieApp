package com.example.admin.themovieapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class ViewModel extends AndroidViewModel
{

    private LiveData<List<Movie>> movies;
    public ViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());
        movies = movieDatabase.movieDao().loadMovies();
    }

    public LiveData<List<Movie>> getMovies()
    {
        return movies;
    }
}
