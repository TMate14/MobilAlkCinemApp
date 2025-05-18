package com.example.cinemapp.models;

import java.io.Serializable;

public class Movie implements Serializable {
    private String title;
    private String genre;
    private String duration;
    private String rating;
    private int imageResId;
    private String description;

    public Movie(String title, String genre, String duration, String rating, int imageResId, String description) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.imageResId = imageResId;
        this.description = description;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getDuration() { return duration; }
    public String getRating() { return rating; }
    public int getImageResId() { return imageResId; }
    public String getDescription() { return description; }
}