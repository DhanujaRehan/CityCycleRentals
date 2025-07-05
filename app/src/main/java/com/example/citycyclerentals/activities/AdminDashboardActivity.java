package com.example.citycyclerentals.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.citycyclerentals.R;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button btnAddBicycle, btnViewCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        btnAddBicycle = findViewById(R.id.btnAddBicycle);
        btnViewCustomers = findViewById(R.id.btnViewCustomers);
    }

    private void setupClickListeners() {
        btnAddBicycle.setOnClickListener(v -> {
            startActivity(new Intent(this, AddBicycleActivity.class));
        });

        btnViewCustomers.setOnClickListener(v -> {
            startActivity(new Intent(this, CustomerListActivity.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}