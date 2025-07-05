package com.example.citycyclerentals.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.citycyclerentals.R;
import com.example.citycyclerentals.models.Bicycle;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class BicycleAdapter extends RecyclerView.Adapter<BicycleAdapter.BicycleViewHolder> {

    private Context context;
    private List<Bicycle> bicycles;
    private OnBicycleClickListener listener;

    public interface OnBicycleClickListener {
        void onAddToCartClicked(Bicycle bicycle);
    }

    public BicycleAdapter(Context context, OnBicycleClickListener listener) {
        this.context = context;
        this.bicycles = new ArrayList<>();
        this.listener = listener;
    }

    public void setBicycles(List<Bicycle> bicycles) {
        this.bicycles = bicycles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BicycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bicycle, parent, false);
        return new BicycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BicycleViewHolder holder, int position) {
        Bicycle bicycle = bicycles.get(position);

        holder.tvDescription.setText(bicycle.getDescription());
        holder.tvPrice.setText("$" + String.format("%.2f", bicycle.getPrice()) + "/day");

        // Load image using Base64 decoding
        if (bicycle.getImage() != null && !bicycle.getImage().isEmpty()) {
            try {
                // Decode Base64 string to bitmap
                byte[] decodedBytes = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    decodedBytes = Base64.getDecoder().decode(bicycle.getImage());
                }
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                if (bitmap != null) {
                    // Create a scaled bitmap for better performance
                    Bitmap scaledBitmap = createScaledBitmap(bitmap, 200, 200);
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

        holder.btnAddToCart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCartClicked(bicycle);
            }
        });
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
        return bicycles.size();
    }

    static class BicycleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBicycle;
        TextView tvDescription, tvPrice;
        Button btnAddToCart;

        public BicycleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBicycle = itemView.findViewById(R.id.ivBicycle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}