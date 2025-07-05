package com.example.citycyclerentals.fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.citycyclerentals.R;
import com.example.citycyclerentals.adapters.BicycleAdapter;
import com.example.citycyclerentals.database.DatabaseHelper;
import com.example.citycyclerentals.models.Bicycle;
import com.example.citycyclerentals.models.CartItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements BicycleAdapter.OnBicycleClickListener {

    private RecyclerView recyclerView;
    private BicycleAdapter adapter;
    private DatabaseHelper dbHelper;
    private int customerId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBicycles);
        dbHelper = new DatabaseHelper(getContext());

        // Get customer ID from shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserSession", MODE_PRIVATE);
        customerId = sharedPreferences.getInt("user_id", 0);

        setupRecyclerView();
        loadBicycles();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BicycleAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);
    }

    private void loadBicycles() {
        List<Bicycle> bicycles = dbHelper.getAllBicycles();
        adapter.setBicycles(bicycles);
    }

    @Override
    public void onAddToCartClicked(Bicycle bicycle) {
        showDateRangePicker(bicycle);
    }

    private void showDateRangePicker(Bicycle bicycle) {
        Calendar calendar = Calendar.getInstance();

        // Show start date picker
        DatePickerDialog startDatePicker = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    String startDate = dayOfMonth + "/" + (month + 1) + "/" + year;

                    // Show end date picker
                    DatePickerDialog endDatePicker = new DatePickerDialog(
                            getContext(),
                            (view2, year2, month2, dayOfMonth2) -> {
                                String endDate = dayOfMonth2 + "/" + (month2 + 1) + "/" + year2;

                                // Calculate total price and add to cart
                                double totalPrice = calculateTotalPrice(bicycle.getPrice(), startDate, endDate);
                                if (totalPrice > 0) {
                                    addToCart(bicycle, startDate, endDate, totalPrice);
                                }
                            },
                            year, month, dayOfMonth);

                    // Set minimum date for end date picker
                    Calendar minEndDate = Calendar.getInstance();
                    minEndDate.set(year, month, dayOfMonth);
                    minEndDate.add(Calendar.DAY_OF_MONTH, 1);
                    endDatePicker.getDatePicker().setMinDate(minEndDate.getTimeInMillis());
                    endDatePicker.setTitle("Select End Date");
                    endDatePicker.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        startDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        startDatePicker.setTitle("Select Start Date");
        startDatePicker.show();
    }

    private double calculateTotalPrice(double dailyPrice, String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            if (start != null && end != null && end.after(start)) {
                long diffInMillies = Math.abs(end.getTime() - start.getTime());
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                return dailyPrice * diffInDays;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void addToCart(Bicycle bicycle, String startDate, String endDate, double totalPrice) {
        CartItem cartItem = new CartItem(customerId, bicycle.getId(), startDate, endDate, totalPrice);
        long result = dbHelper.addToCart(cartItem);

        if (result != -1) {
            Toast.makeText(getContext(), "Added to cart successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show();
        }
    }
}