package com.example.admin.themovieapp;

import com.google.gson.annotations.SerializedName;

public class Reviews
{

        @SerializedName("author")
        private String author;

        @SerializedName("content")
        private String content;

        public Reviews(String author , String content)
        {
            this.author = author;
            this.content= content;
        }

        public String getAuthor()
        {
            return this.author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getDetails()
        {
            return content;
        }

        public void setDetails(String details) {
            this.content = details;
        }

}
