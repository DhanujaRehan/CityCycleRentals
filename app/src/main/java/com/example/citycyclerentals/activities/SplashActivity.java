package com.example.citycyclerentals.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.citycyclerentals.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            checkLoginStatus();
        }, SPLASH_DURATION);
    }

    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);

        Intent intent;
        if (isLoggedIn) {
            boolean isAdmin = sharedPreferences.getBoolean("is_admin", false);
            if (isAdmin) {
                intent = new Intent(this, AdminDashboardActivity.class);
            } else {
                intent = new Intent(this, CustomerDashboardActivity.class);
            }
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}