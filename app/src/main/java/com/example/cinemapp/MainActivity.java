package com.example.cinemapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ViewPager2 imageViewPager;
    private List<String> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        Button loginButton = (Button)findViewById(R.id.button2);
        Button registrationButton = (Button)findViewById(R.id.button);
        Button profileButton = (Button)findViewById(R.id.profileButton);
        Button showMovies = (Button)findViewById(R.id.showMovies);

        if (mAuth.getCurrentUser() != null) {
            registrationButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
        } else {
            profileButton.setVisibility(View.GONE);
            showMovies.setVisibility(View.GONE);
        }

        // Carousel
        imageViewPager = findViewById(R.id.imageViewPager);

        // Animation - fade in
        imageViewPager.setAlpha(0f);
        imageViewPager.setTranslationY(-100f);

        imageViewPager.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(1000)
                .setStartDelay(300)
                .start();


        // Sample image URLs
        List<Integer> imageResourceIds = Arrays.asList(
                R.drawable.midnight_coder,
                R.drawable.ai_apocalipse,
                R.drawable.fire_teacher,
                R.drawable.galactic_command,
                R.drawable.galactus,
                R.drawable.math,
                R.drawable.revenge_of_the_ducks,
                R.drawable.romantic_dinner
        );

        ImageAdapter adapter = new ImageAdapter(this, imageResourceIds);
        imageViewPager.setAdapter(adapter);
    }

    public void navigateToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void navigateToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void navigateToProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void test(View view) {
        Intent intent = new Intent(this, WeeklyMovieActivity.class);
        startActivity(intent);
    }

    // Tap animation
    public void onCoverClicked(View view) {
        view.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(150)
                .withEndAction(() -> {
                    view.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(150)
                            .start();
                })
                .start();
    }
}