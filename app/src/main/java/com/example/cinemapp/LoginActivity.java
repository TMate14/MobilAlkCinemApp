package com.example.cinemapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText emailLoginEntry, passwordLoginEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void loginPress(View view) {
        emailLoginEntry = findViewById(R.id.emailLogin);
        passwordLoginEntry = findViewById(R.id.passwordLogin);
        mAuth = FirebaseAuth.getInstance();

        String email = emailLoginEntry.getText().toString().trim();
        String password = passwordLoginEntry.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            Toast.makeText(this, "Sikeres bejelentkezés", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Sikertelen belejentkezés", Toast.LENGTH_SHORT).show();
        });
    }

    public void backPress(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
