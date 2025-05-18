package com.example.cinemapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    TextView textUserName, textUserEmail, textPhone, textUserGenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Link views
        textUserName = findViewById(R.id.textUserName);
        textUserEmail = findViewById(R.id.textUserEmail);
        textPhone = findViewById(R.id.textPhone);
        textUserGenre = findViewById(R.id.textUserGenre);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            db.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            String phone = documentSnapshot.getString("phone");
                            String genre = documentSnapshot.getString("genre");

                            textUserName.setText("Név: " + name + " 🖋");
                            textUserEmail.setText("E-mail: " + email);
                            textPhone.setText("Telefonszám: " + phone + " 🖋");
                            textUserGenre.setText("Kedvenceid a(z) " + genre + " filmek 🖋");
                        } else {
                            Toast.makeText(this, "Nem található felhasználói adat", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Hiba az adatok betöltésekor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Nincs bejelentkezett felhasználó", Toast.LENGTH_SHORT).show();
        }

        textUserName.setOnClickListener(v -> showEditDialog("Név", "name", textUserName));
        textPhone.setOnClickListener(v -> showEditDialog("Telefonszám", "phone", textPhone));
        textUserGenre.setOnClickListener(v -> showEditDialog("Kedvenc műfaj", "genre", textUserGenre));

        loadUserBookings();
    }

    private void showEditDialog(String title, String fieldKey, TextView targetView) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        String uid = currentUser.getUid();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        if (fieldKey.equals("genre")) {
            // Spinner-based dialog for genre selection
            final Spinner genreSpinner = new Spinner(this);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.movie_genres,
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            genreSpinner.setAdapter(adapter);

            // Optionally, pre-select current genre
            String currentGenre = targetView.getText().toString().replaceAll(".* a\\(z\\) ", "").replace(" filmek 🖋", "");
            int position = adapter.getPosition(currentGenre);
            if (position >= 0) genreSpinner.setSelection(position);

            builder.setView(genreSpinner);

            builder.setPositiveButton("Mentés", (dialog, which) -> {
                String selectedGenre = genreSpinner.getSelectedItem().toString();

                db.collection("users").document(uid)
                        .update(fieldKey, selectedGenre)
                        .addOnSuccessListener(aVoid -> {
                            targetView.setText("Kedvenceid a(z) " + selectedGenre + " filmek 🖋");
                            Toast.makeText(this, "Műfaj frissítve", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            });

        } else {
            // EditText-based dialog for name/phone
            final EditText input = new EditText(this);
            String currentText = targetView.getText().toString();
            String currentValue = currentText.contains(":") ? currentText.split(": ")[1].replace(" 🖋", "") : "";
            input.setText(currentValue);
            builder.setView(input);

            builder.setPositiveButton("Mentés", (dialog, which) -> {
                String newValue = input.getText().toString().trim();

                if (!newValue.isEmpty()) {
                    db.collection("users").document(uid)
                            .update(fieldKey, newValue)
                            .addOnSuccessListener(aVoid -> {
                                switch (fieldKey) {
                                    case "name":
                                        targetView.setText("Név: " + newValue + " 🖋");
                                        break;
                                    case "phone":
                                        targetView.setText("Telefonszám: " + newValue + " 🖋");
                                        break;
                                }
                                Toast.makeText(this, "Sikeres frissítés", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            });
        }

        builder.setNegativeButton("Mégse", (dialog, which) -> dialog.cancel());
        builder.show();
    }


    public void logoutUser(View view) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void backPress(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void loadUserBookings() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        String uid = currentUser.getUid();
        LinearLayout layoutBookingsContainer = findViewById(R.id.layoutBookingsContainer);
        layoutBookingsContainer.removeAllViews(); // Clear previous

        db.collection("bookings")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (var document : queryDocumentSnapshots) {
                        String bookingId = document.getId();
                        String movie = document.getString("movieTitle");
                        String time = document.getString("time");
                        String seat = document.getString("seat");

                        View bookingView = getLayoutInflater().inflate(R.layout.booking_item, null);
                        TextView bookingText = bookingView.findViewById(R.id.textBookingDetails);
                        Button deleteButton = bookingView.findViewById(R.id.buttonDeleteBooking);

                        bookingText.setText(movie + " - " + time + " - Hely: " + seat);

                        deleteButton.setOnClickListener(v -> {
                            db.collection("bookings").document(bookingId)
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        layoutBookingsContainer.removeView(bookingView);
                                        Toast.makeText(this, "Foglalás törölve", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        });

                        layoutBookingsContainer.addView(bookingView);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Hiba a foglalások betöltésekor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}