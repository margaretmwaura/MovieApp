package com.example.admin.themovieapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


//This data is for one movie
@Entity(tableName = "Movies")
public class Movie implements Parcelable,Comparable<Movie>
{

    @SerializedName("poster_path")
    private String posterPath;

//   This is the synopsis
    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String releaseDate;


    @PrimaryKey
    @SerializedName("id")
    private Integer id;

    @SerializedName("original_title")
    private String originalTitle;


    @SerializedName("title")
    private String title;


    @SerializedName("vote_average")
    private Double voteAverage;

    public Movie(String posterPath, String overview, String releaseDate,Integer id,
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


//    The parcelling bit

    public Movie(Parcel in)
    {
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        id = in.readInt();
        originalTitle = in.readString();
        title = in.readString();
        voteAverage = in.readDouble();
    }
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in)
        {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeInt(id);
        dest.writeString(originalTitle);

        dest.writeString(title);
        dest.writeDouble(voteAverage);
    }


    @Override
    public int compareTo(@NonNull Movie o)
    {
        int interger1 = this.getVoteAverage().intValue();
        int interger2 = o.getVoteAverage().intValue();

        return interger2 - interger1;
    }
}
