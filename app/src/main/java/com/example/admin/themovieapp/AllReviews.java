package com.example.admin.themovieapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllReviews
{
    @SerializedName("results")
    private List<Reviews> reviews;

    public List<Reviews> getReviews() {
        return reviews;
    }

    public void setReviews(List<Reviews> reviews) {
        this.reviews = reviews;
    }
}
