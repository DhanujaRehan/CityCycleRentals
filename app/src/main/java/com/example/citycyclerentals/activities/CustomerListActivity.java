package com.example.citycyclerentals.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.citycyclerentals.R;
import com.example.citycyclerentals.adapters.CustomerAdapter;
import com.example.citycyclerentals.database.DatabaseHelper;
import com.example.citycyclerentals.models.Customer;

import java.util.List;

public class CustomerListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvEmptyList;
    private CustomerAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        initializeViews();
        dbHelper = new DatabaseHelper(this);

        setupRecyclerView();
        loadCustomers();

        // Set up action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Customer List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewCustomers);
        tvEmptyList = findViewById(R.id.tvEmptyList);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomerAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void loadCustomers() {
        List<Customer> customers = dbHelper.getAllCustomers();

        if (customers.isEmpty()) {
            tvEmptyList.setVisibility(android.view.View.VISIBLE);
            recyclerView.setVisibility(android.view.View.GONE);
        } else {
            tvEmptyList.setVisibility(android.view.View.GONE);
            recyclerView.setVisibility(android.view.View.VISIBLE);
            adapter.setCustomers(customers);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}