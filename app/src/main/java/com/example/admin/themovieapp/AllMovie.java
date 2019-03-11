package com.example.admin.themovieapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;


//This data helps us get the count of all the movies

public class AllMovie
{

    @SerializedName("results")
    private List<Movie> results;
    @SerializedName("total_results")
    private int totalResults;

    public List<Movie> getResults() {
        return results;
    }
    public void setResults(List<Movie> results) {
        this.results = results;
    }
    public int getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

}

