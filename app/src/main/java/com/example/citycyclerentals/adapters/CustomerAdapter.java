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
import com.example.citycyclerentals.models.Customer;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private Context context;
    private List<Customer> customers;

    public CustomerAdapter(Context context) {
        this.context = context;
        this.customers = new ArrayList<>();
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customers.get(position);

        holder.tvUsername.setText(customer.getUsername());
        holder.tvEmail.setText(customer.getEmail());
        holder.tvPhone.setText("Phone: " + (customer.getPhone() != null ? customer.getPhone() : "N/A"));
        holder.tvIdNumber.setText("ID: " + (customer.getIdNumber() != null ? customer.getIdNumber() : "N/A"));
        holder.tvBirthday.setText("Birthday: " + (customer.getBirthday() != null ? customer.getBirthday() : "N/A"));

        // Load profile picture
        if (customer.getProfilePicture() != null && !customer.getProfilePicture().isEmpty()) {
            try {
                byte[] decodedBytes = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    decodedBytes = Base64.getDecoder().decode(customer.getProfilePicture());
                }
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                holder.ivProfilePicture.setImageBitmap(bitmap);
            } catch (Exception e) {
                holder.ivProfilePicture.setImageResource(R.drawable.ic_person);
            }
        } else {
            holder.ivProfilePicture.setImageResource(R.drawable.ic_person);
        }
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    static class CustomerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfilePicture;
        TextView tvUsername, tvEmail, tvPhone, tvIdNumber, tvBirthday;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePicture = itemView.findViewById(R.id.ivProfilePicture);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvIdNumber = itemView.findViewById(R.id.tvIdNumber);
            tvBirthday = itemView.findViewById(R.id.tvBirthday);
        }
    }
}