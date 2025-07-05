package com.example.citycyclerentals.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.citycyclerentals.R;
import com.example.citycyclerentals.adapters.CartAdapter;
import com.example.citycyclerentals.database.DatabaseHelper;
import com.example.citycyclerentals.models.CartItem;
import com.example.citycyclerentals.models.RentalHistory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvTotalAmount, tvEmptyCart;
    private Button btnCheckout;
    private CartAdapter adapter;
    private DatabaseHelper dbHelper;
    private int customerId;
    private List<CartItem> cartItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        initializeViews(view);
        dbHelper = new DatabaseHelper(getContext());

        // Get customer ID from shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserSession", MODE_PRIVATE);
        customerId = sharedPreferences.getInt("user_id", 0);

        setupRecyclerView();
        loadCartItems();

        btnCheckout.setOnClickListener(v -> showCheckoutDialog());

        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewCart);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        tvEmptyCart = view.findViewById(R.id.tvEmptyCart);
        btnCheckout = view.findViewById(R.id.btnCheckout);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    private void loadCartItems() {
        cartItems = dbHelper.getCartItems(customerId);

        if (cartItems.isEmpty()) {
            tvEmptyCart.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            tvTotalAmount.setVisibility(View.GONE);
            btnCheckout.setVisibility(View.GONE);
        } else {
            tvEmptyCart.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            tvTotalAmount.setVisibility(View.VISIBLE);
            btnCheckout.setVisibility(View.VISIBLE);

            adapter.setCartItems(cartItems);
            calculateTotal();
        }
    }

    private void calculateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        tvTotalAmount.setText("Total: $" + String.format("%.2f", total));
    }

    private void showCheckoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_checkout, null);

        TextView tvMessage = dialogView.findViewById(R.id.tvCheckoutMessage);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirmCheckout);
        Button btnCancel = dialogView.findViewById(R.id.btnCancelCheckout);

        tvMessage.setText("Safe Paddle!\n\nConfirm your rental booking?");

        AlertDialog dialog = builder.setView(dialogView).create();

        btnConfirm.setOnClickListener(v -> {
            processCheckout();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void processCheckout() {
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        // Move cart items to rental history
        for (CartItem cartItem : cartItems) {
            RentalHistory rentalHistory = new RentalHistory(
                    cartItem.getCustomerId(),
                    cartItem.getBicycleId(),
                    cartItem.getStartDate(),
                    cartItem.getEndDate(),
                    cartItem.getTotalPrice(),
                    currentDate
            );
            dbHelper.addToRentalHistory(rentalHistory);
        }

        // Clear cart
        dbHelper.clearCart(customerId);

        Toast.makeText(getContext(), "Checkout successful! Enjoy your ride!", Toast.LENGTH_LONG).show();

        // Refresh cart view
        loadCartItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCartItems();
    }
}