package com.example.citycyclerentals;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.citycyclerentals.R;
import com.example.citycyclerentals.database.DatabaseHelper;
import com.example.citycyclerentals.models.Customer;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword, etPhone, etIdNumber;
    private TextView tvBirthday;
    private Button btnRegister, btnSelectBirthday;
    private TextView tvLogin;
    private DatabaseHelper dbHelper;
    private String selectedBirthday = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeViews();
        dbHelper = new DatabaseHelper(this);

        btnSelectBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initializeViews() {
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);
        etIdNumber = findViewById(R.id.etIdNumber);
        tvBirthday = findViewById(R.id.tvBirthday);
        btnRegister = findViewById(R.id.btnRegister);
        btnSelectBirthday = findViewById(R.id.btnSelectBirthday);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedBirthday = dayOfMonth + "/" + (month + 1) + "/" + year;
                        tvBirthday.setText(selectedBirthday);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void register() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String idNumber = etIdNumber.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() ||
                confirmPassword.isEmpty() || phone.isEmpty() || idNumber.isEmpty() ||
                selectedBirthday.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        Customer customer = new Customer(username, email, password, phone, idNumber, selectedBirthday, "");
        long result = dbHelper.addCustomer(customer);

        if (result != -1) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Email might already exist.", Toast.LENGTH_SHORT).show();
        }
    }
}