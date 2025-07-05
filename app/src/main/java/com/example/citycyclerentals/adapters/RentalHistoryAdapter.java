package com.example.citycyclerentals.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.citycyclerentals.R;
import com.example.citycyclerentals.models.RentalHistory;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class RentalHistoryAdapter extends RecyclerView.Adapter<RentalHistoryAdapter.HistoryViewHolder> {

    private Context context;
    private List<RentalHistory> rentalHistories;

    public RentalHistoryAdapter(Context context) {
        this.context = context;
        this.rentalHistories = new ArrayList<>();
    }

    public void setRentalHistories(List<RentalHistory> rentalHistories) {
        this.rentalHistories = rentalHistories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rental_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        RentalHistory rentalHistory = rentalHistories.get(position);

        holder.tvDescription.setText(rentalHistory.getBicycleDescription());
        holder.tvStartDate.setText("From: " + rentalHistory.getStartDate());
        holder.tvEndDate.setText("To: " + rentalHistory.getEndDate());
        holder.tvRentalDate.setText("Rented: " + rentalHistory.getRentalDate());
        holder.tvPrice.setText("$" + String.format("%.2f", rentalHistory.getTotalPrice()));

        // Load image using Base64 decoding
        if (rentalHistory.getBicycleImage() != null && !rentalHistory.getBicycleImage().isEmpty()) {
            try {
                // Decode Base64 string to bitmap
                byte[] decodedBytes = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    decodedBytes = Base64.getDecoder().decode(rentalHistory.getBicycleImage());
                }
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
        return rentalHistories.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBicycle;
        TextView tvDescription, tvStartDate, tvEndDate, tvRentalDate, tvPrice;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBicycle = itemView.findViewById(R.id.ivBicycle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvRentalDate = itemView.findViewById(R.id.tvRentalDate);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}