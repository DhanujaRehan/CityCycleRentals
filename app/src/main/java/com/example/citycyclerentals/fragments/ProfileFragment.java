package com.example.citycyclerentals.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.citycyclerentals.R;
import com.example.citycyclerentals.database.DatabaseHelper;
import com.example.citycyclerentals.models.Customer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private ImageView ivProfilePicture;
    private EditText etUsername, etEmail, etPhone, etIdNumber;
    private TextView tvBirthday;
    private Button btnSelectBirthday, btnEditPicture, btnSaveProfile;
    private DatabaseHelper dbHelper;
    private Customer currentCustomer;
    private String selectedBirthday = "";
    private String encodedImage = "";

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeViews(view);
        dbHelper = new DatabaseHelper(getContext());

        setupImagePicker();
        loadUserProfile();
        setupClickListeners();

        return view;
    }

    private void initializeViews(View view) {
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);
        etUsername = view.findViewById(R.id.etUsername);
        etEmail = view.findViewById(R.id.etEmail);
        etPhone = view.findViewById(R.id.etPhone);
        etIdNumber = view.findViewById(R.id.etIdNumber);
        tvBirthday = view.findViewById(R.id.tvBirthday);
        btnSelectBirthday = view.findViewById(R.id.btnSelectBirthday);
        btnEditPicture = view.findViewById(R.id.btnEditPicture);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                                ivProfilePicture.setImageBitmap(bitmap);
                                encodedImage = encodeImageToBase64(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserSession", MODE_PRIVATE);
        int customerId = sharedPreferences.getInt("user_id", 0);

        currentCustomer = dbHelper.getCustomerById(customerId);
        if (currentCustomer != null) {
            etUsername.setText(currentCustomer.getUsername());
            etEmail.setText(currentCustomer.getEmail());
            etPhone.setText(currentCustomer.getPhone());
            etIdNumber.setText(currentCustomer.getIdNumber());
            tvBirthday.setText(currentCustomer.getBirthday());
            selectedBirthday = currentCustomer.getBirthday();

            // Load profile picture
            if (currentCustomer.getProfilePicture() != null && !currentCustomer.getProfilePicture().isEmpty()) {
                try {
                    byte[] decodedBytes = Base64.getDecoder().decode(currentCustomer.getProfilePicture());
                    Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    ivProfilePicture.setImageBitmap(bitmap);
                } catch (Exception e) {
                    ivProfilePicture.setImageResource(R.drawable.ic_person);
                }
            }
        }
    }

    private void setupClickListeners() {
        btnEditPicture.setOnClickListener(v -> openImagePicker());

        btnSelectBirthday.setOnClickListener(v -> showDatePicker());

        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedBirthday = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    tvBirthday.setText(selectedBirthday);
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void saveProfile() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String idNumber = etIdNumber.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || idNumber.isEmpty() || selectedBirthday.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        currentCustomer.setUsername(username);
        currentCustomer.setEmail(email);
        currentCustomer.setPhone(phone);
        currentCustomer.setIdNumber(idNumber);
        currentCustomer.setBirthday(selectedBirthday);

        if (!encodedImage.isEmpty()) {
            currentCustomer.setProfilePicture(encodedImage);
        }

        boolean result = dbHelper.updateCustomer(currentCustomer);
        if (result) {
            Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}