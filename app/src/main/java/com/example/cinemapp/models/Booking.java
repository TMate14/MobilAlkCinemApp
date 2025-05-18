package com.example.cinemapp.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;

public class Booking implements Serializable {
    private String userId;
    private String movieTitle;
    private String time;
    private String seat;
    private int weekIndex;
    @ServerTimestamp
    private Timestamp timestamp;

    // Empty constructor required for Firestore
    public Booking() {}

    public Booking(String userId, String movieTitle, String time, String seat, int weekIndex) {
        this.userId = userId;
        this.movieTitle = movieTitle;
        this.time = time;
        this.seat = seat;
        this.weekIndex = weekIndex;
    }

    // Getters and setters for Firestore and serialization
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getSeat() { return seat; }
    public void setSeat(String seat) { this.seat = seat; }

    public int getWeekIndex() { return weekIndex; }
    public void setWeekIndex(int weekIndex) { this.weekIndex = weekIndex; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}