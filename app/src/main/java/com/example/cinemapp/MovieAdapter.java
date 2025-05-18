package com.example.cinemapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemapp.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private final Context context;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movieList = movies;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText("Műfaj: " + movie.getGenre());
        holder.duration.setText("Időtartam: " + movie.getDuration());
        holder.rating.setText("Értékelés: " + movie.getRating());
        holder.cover.setImageResource(movie.getImageResId());

        holder.buttonDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra("movie", movie);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title, genre, duration, rating;
        ImageView cover;
        Button buttonDetails;

        public MovieViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textMovieTitle);
            genre = itemView.findViewById(R.id.textMovieGenre);
            duration = itemView.findViewById(R.id.textMovieDuration);
            rating = itemView.findViewById(R.id.textMovieRating);
            cover = itemView.findViewById(R.id.imageMovieCover);
            buttonDetails = itemView.findViewById(R.id.buttonDetails);
        }
    }
}