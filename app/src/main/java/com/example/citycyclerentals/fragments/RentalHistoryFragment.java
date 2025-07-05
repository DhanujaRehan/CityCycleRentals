package com.example.citycyclerentals.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.citycyclerentals.R;
import com.example.citycyclerentals.adapters.RentalHistoryAdapter;
import com.example.citycyclerentals.database.DatabaseHelper;
import com.example.citycyclerentals.models.RentalHistory;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RentalHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvEmptyHistory;
    private RentalHistoryAdapter adapter;
    private DatabaseHelper dbHelper;
    private int customerId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rental_history, container, false);

        initializeViews(view);
        dbHelper = new DatabaseHelper(getContext());

        // Get customer ID from shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserSession", MODE_PRIVATE);
        customerId = sharedPreferences.getInt("user_id", 0);

        setupRecyclerView();
        loadRentalHistory();

        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewHistory);
        tvEmptyHistory = view.findViewById(R.id.tvEmptyHistory);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RentalHistoryAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    private void loadRentalHistory() {
        List<RentalHistory> rentalHistories = dbHelper.getRentalHistory(customerId);

        if (rentalHistories.isEmpty()) {
            tvEmptyHistory.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmptyHistory.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setRentalHistories(rentalHistories);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRentalHistory();
    }
}