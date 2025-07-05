package com.example.citycyclerentals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.citycyclerentals.R;
import com.example.citycyclerentals.models.Bicycle;

import java.util.ArrayList;
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

        // Load image using Glide
        if (bicycle.getImage() != null && !bicycle.getImage().isEmpty()) {
            Glide.with(context)
                    .load(bicycle.getImage())
                    .placeholder(R.drawable.ic_bike)
                    .error(R.drawable.ic_bike)
                    .into(holder.ivBicycle);
        } else {
            holder.ivBicycle.setImageResource(R.drawable.ic_bike);
        }

        holder.btnAddToCart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCartClicked(bicycle);
            }
        });
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