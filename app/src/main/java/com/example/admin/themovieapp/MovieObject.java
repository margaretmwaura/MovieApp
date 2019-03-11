package com.example.admin.themovieapp;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Movies")
public class MovieObject
{

    private String posterPath;
    private String overview;
    private String releaseDate;
    @PrimaryKey
    private Integer id;
    private String originalTitle;
    private String title;
    private Double voteAverage;

    public MovieObject(String posterPath, String overview, String releaseDate,Integer id,
                 String originalTitle, String title,
                 Double voteAverage) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.originalTitle = originalTitle;
        this.title = title;
        this.voteAverage = voteAverage;
    }
    public String getPosterPath() {
        return posterPath;
    }
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getOriginalTitle() {
        return originalTitle;
    }
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Double getVoteAverage() {
        return voteAverage;
    }
    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }
}
