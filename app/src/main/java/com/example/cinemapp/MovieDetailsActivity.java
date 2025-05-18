package com.example.cinemapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemapp.models.Booking;
import com.example.cinemapp.models.Movie;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MovieDetailsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView titleTextView, descriptionTextView;
    private ImageView coverImageView;
    private RadioGroup timeRadioGroup;
    private Spinner seatSpinner;
    private Button bookButton;
    private Movie selectedMovie;
    private String selectedTime = "Szombat 13:00";
    private String selectedSeat = "Közép-közép";
    private int selectedWeekIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Bind views
        titleTextView = findViewById(R.id.movieTitle);
        descriptionTextView = findViewById(R.id.movieDescription);
        coverImageView = findViewById(R.id.movieImage);
        timeRadioGroup = findViewById(R.id.timeRadioGroup);
        seatSpinner = findViewById(R.id.seatSpinner);
        bookButton = findViewById(R.id.bookButton);

        // Get movie from intent
        Intent intent = getIntent();
        selectedMovie = (Movie) intent.getSerializableExtra("movie");
        selectedWeekIndex = getIntent().getIntExtra("weekIndex", 0); // default: current week

        if (selectedMovie != null) {
            titleTextView.setText(selectedMovie.getTitle());
            descriptionTextView.setText(selectedMovie.getDescription());
            coverImageView.setImageResource(selectedMovie.getImageResId());
        }

        // Populate seat options
        ArrayAdapter<CharSequence> seatAdapter = ArrayAdapter.createFromResource(this,
                R.array.seat_options, android.R.layout.simple_spinner_item);
        seatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatSpinner.setAdapter(seatAdapter);
        seatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView)parentView.getChildAt(0)).setTextColor(Color.WHITE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Handle time selection
        timeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selected = findViewById(checkedId);
            if (selected != null) {
                selectedTime = selected.getText().toString();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        bookButton.setOnClickListener(v -> saveBooking());
    }

    private void saveBooking() {
        String userId = mAuth.getCurrentUser().getUid();

        selectedSeat = seatSpinner.getSelectedItem().toString();
        if (selectedTime.isEmpty()) {
            Toast.makeText(this, "Válassz egy időpontot!", Toast.LENGTH_SHORT).show();
            return;
        }

        Booking booking = new Booking(userId, selectedMovie.getTitle(), selectedTime, selectedSeat, selectedWeekIndex);

        db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Foglalás sikeres!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Hiba a foglalás mentésekor: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    public void backPress(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void navigateToProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }
}
