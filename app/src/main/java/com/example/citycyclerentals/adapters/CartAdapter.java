package com.example.citycyclerentals.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.citycyclerentals.R;
import com.example.citycyclerentals.models.CartItem;

import java.util.ArrayList;
import java.util.Base64;
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

        // Load image using Base64 decoding
        if (cartItem.getBicycleImage() != null && !cartItem.getBicycleImage().isEmpty()) {
            try {
                // Decode Base64 string to bitmap
                byte[] decodedBytes = Base64.getDecoder().decode(cartItem.getBicycleImage());
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                if (bitmap != null) {
                    // Create a scaled bitmap for better performance
                    Bitmap scaledBitmap = createScaledBitmap(bitmap, 150, 150);
                    holder.ivBicycle.setImageBitmap(scaledBitmap);

                    // Recycle original bitmap to free memory
                    if (bitmap != scaledBitmap) {
                        bitmap.recycle();
                    }
                } else {
                    holder.ivBicycle.setImageResource(R.drawable.ic_bike);
                }
            } catch (Exception e) {
                e.printStackTrace();
                holder.ivBicycle.setImageResource(R.drawable.ic_bike);
            }
        } else {
            holder.ivBicycle.setImageResource(R.drawable.ic_bike);
        }
    }

    /**
     * Create a scaled bitmap for better memory management
     */
    private Bitmap createScaledBitmap(Bitmap originalBitmap, int maxWidth, int maxHeight) {
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();

        // Calculate scaling factor
        float scaleFactor = Math.min(
                (float) maxWidth / originalWidth,
                (float) maxHeight / originalHeight
        );

        // If the image is already smaller, don't scale up
        if (scaleFactor >= 1.0f) {
            return originalBitmap;
        }

        int newWidth = Math.round(originalWidth * scaleFactor);
        int newHeight = Math.round(originalHeight * scaleFactor);

        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
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