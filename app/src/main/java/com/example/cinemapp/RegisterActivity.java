package com.example.cinemapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    EditText nameRegisterEntry, emailRegisterEntry, passwordRegisterEntry, passwordConfirmEntry, phoneRegisterEntry;
    Spinner genreSpinnerEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        genreSpinnerEntry = findViewById(R.id.genreSpinner);
        genreSpinnerEntry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView)parentView.getChildAt(0)).setTextColor(Color.WHITE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    public void registerPress(View view) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameRegisterEntry = findViewById(R.id.nameRegister);
        emailRegisterEntry = findViewById(R.id.emailRegister);
        passwordRegisterEntry = findViewById(R.id.passwordRegister);
        passwordConfirmEntry = findViewById(R.id.passwordRegisterConfirm);
        phoneRegisterEntry = findViewById(R.id.phoneRegister);
        genreSpinnerEntry = findViewById(R.id.genreSpinner);

        String name = nameRegisterEntry.getText().toString().trim();
        String email = emailRegisterEntry.getText().toString().trim();
        String password = passwordRegisterEntry.getText().toString().trim();
        String passwordComfirm = passwordConfirmEntry.getText().toString().trim();
        String phone = phoneRegisterEntry.getText().toString().trim();
        String genre = genreSpinnerEntry.getSelectedItem().toString();

        if (name.length() <= 20 && !name.isEmpty()) {
            if (!email.isEmpty()) {
                if (password.equals(passwordComfirm)) {
                    if (password.length() > 5) {
                        if (phone.length() <= 20 && !phone.isEmpty()) {
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(this, task -> {
                                        if (task.isSuccessful()) {
                                            String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                                            Map<String, Object> user = new HashMap<>();
                                            user.put("name", name);
                                            user.put("email", email);
                                            user.put("phone", phone);
                                            user.put("genre", genre);

                                            db.collection("users").document(userId)
                                                    .set(user)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(RegisterActivity.this, "Sikeres regisztráció", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                                        finish();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(RegisterActivity.this, "Hiba a Firestore mentésnél: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    });
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(this, "Helytelen telefonszám", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Túl rövid jelszó", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Nem megegyező jelszavak", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "E-mail hiba", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "A felhasználónév max 20 karakter lehet", Toast.LENGTH_SHORT).show();
        }
    }

    public void backPress(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
