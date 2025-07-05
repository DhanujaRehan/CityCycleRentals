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
import com.example.citycyclerentals.models.RentalHistory;

import java.util.ArrayList;
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

        // Load image using Glide
        if (rentalHistory.getBicycleImage() != null && !rentalHistory.getBicycleImage().isEmpty()) {
            Glide.with(context)
                    .load(rentalHistory.getBicycleImage())
                    .placeholder(R.drawable.ic_bike)
                    .error(R.drawable.ic_bike)
                    .into(holder.ivBicycle);
        } else {
            holder.ivBicycle.setImageResource(R.drawable.ic_bike);
        }
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