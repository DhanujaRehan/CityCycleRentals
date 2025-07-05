package com.example.citycyclerentals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.citycyclerentals.R;
import com.example.citycyclerentals.models.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;

    public CartAdapter(Context context) {
        this.context = context;
        this.cartItems = new ArrayList<>();
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.tvDescription.setText(cartItem.getBicycleDescription());
        holder.tvStartDate.setText("From: " + cartItem.getStartDate());
        holder.tvEndDate.setText("To: " + cartItem.getEndDate());
        holder.tvPrice.setText("$" + String.format("%.2f", cartItem.getTotalPrice()));

        // Load image using Glide
        if (cartItem.getBicycleImage() != null && !cartItem.getBicycleImage().isEmpty()) {
            Glide.with(context)
                    .load(cartItem.getBicycleImage())
                    .placeholder(R.drawable.ic_bike)
                    .error(R.drawable.ic_bike)
                    .into(holder.ivBicycle);
        } else {
            holder.ivBicycle.setImageResource(R.drawable.ic_bike);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBicycle;
        TextView tvDescription, tvStartDate, tvEndDate, tvPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBicycle = itemView.findViewById(R.id.ivBicycle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}