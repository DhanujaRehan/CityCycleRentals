package com.example.citycyclerentals;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.citycyclerentals.models.Bicycle;
import com.example.citycyclerentals.models.CartItem;
import com.example.citycyclerentals.models.Customer;
import com.example.citycyclerentals.models.RentalHistory;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CityCycleRentals.db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_CUSTOMERS = "customers";
    private static final String TABLE_BICYCLES = "bicycles";
    private static final String TABLE_CART = "cart";
    private static final String TABLE_RENTAL_HISTORY = "rental_history";

    // Customer table columns
    private static final String CUSTOMER_ID = "id";
    private static final String CUSTOMER_USERNAME = "username";
    private static final String CUSTOMER_EMAIL = "email";
    private static final String CUSTOMER_PASSWORD = "password";
    private static final String CUSTOMER_PHONE = "phone";
    private static final String CUSTOMER_ID_NUMBER = "id_number";
    private static final String CUSTOMER_BIRTHDAY = "birthday";
    private static final String CUSTOMER_PROFILE_PICTURE = "profile_picture";

    // Bicycle table columns
    private static final String BICYCLE_ID = "id";
    private static final String BICYCLE_IMAGE = "image";
    private static final String BICYCLE_PRICE = "price";
    private static final String BICYCLE_DESCRIPTION = "description";

    // Cart table columns
    private static final String CART_ID = "id";
    private static final String CART_CUSTOMER_ID = "customer_id";
    private static final String CART_BICYCLE_ID = "bicycle_id";
    private static final String CART_START_DATE = "start_date";
    private static final String CART_END_DATE = "end_date";
    private static final String CART_TOTAL_PRICE = "total_price";

    // Rental history table columns
    private static final String RENTAL_ID = "id";
    private static final String RENTAL_CUSTOMER_ID = "customer_id";
    private static final String RENTAL_BICYCLE_ID = "bicycle_id";
    private static final String RENTAL_START_DATE = "start_date";
    private static final String RENTAL_END_DATE = "end_date";
    private static final String RENTAL_TOTAL_PRICE = "total_price";
    private static final String RENTAL_DATE = "rental_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create customers table
        String CREATE_CUSTOMERS_TABLE = "CREATE TABLE " + TABLE_CUSTOMERS + "("
                + CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CUSTOMER_USERNAME + " TEXT NOT NULL,"
                + CUSTOMER_EMAIL + " TEXT NOT NULL UNIQUE,"
                + CUSTOMER_PASSWORD + " TEXT NOT NULL,"
                + CUSTOMER_PHONE + " TEXT,"
                + CUSTOMER_ID_NUMBER + " TEXT,"
                + CUSTOMER_BIRTHDAY + " TEXT,"
                + CUSTOMER_PROFILE_PICTURE + " TEXT" + ")";

        // Create bicycles table
        String CREATE_BICYCLES_TABLE = "CREATE TABLE " + TABLE_BICYCLES + "("
                + BICYCLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BICYCLE_IMAGE + " TEXT,"
                + BICYCLE_PRICE + " REAL NOT NULL,"
                + BICYCLE_DESCRIPTION + " TEXT" + ")";

        // Create cart table
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CART_CUSTOMER_ID + " INTEGER,"
                + CART_BICYCLE_ID + " INTEGER,"
                + CART_START_DATE + " TEXT,"
                + CART_END_DATE + " TEXT,"
                + CART_TOTAL_PRICE + " REAL,"
                + "FOREIGN KEY(" + CART_CUSTOMER_ID + ") REFERENCES " + TABLE_CUSTOMERS + "(" + CUSTOMER_ID + "),"
                + "FOREIGN KEY(" + CART_BICYCLE_ID + ") REFERENCES " + TABLE_BICYCLES + "(" + BICYCLE_ID + ")" + ")";

        // Create rental history table
        String CREATE_RENTAL_HISTORY_TABLE = "CREATE TABLE " + TABLE_RENTAL_HISTORY + "("
                + RENTAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RENTAL_CUSTOMER_ID + " INTEGER,"
                + RENTAL_BICYCLE_ID + " INTEGER,"
                + RENTAL_START_DATE + " TEXT,"
                + RENTAL_END_DATE + " TEXT,"
                + RENTAL_TOTAL_PRICE + " REAL,"
                + RENTAL_DATE + " TEXT,"
                + "FOREIGN KEY(" + RENTAL_CUSTOMER_ID + ") REFERENCES " + TABLE_CUSTOMERS + "(" + CUSTOMER_ID + "),"
                + "FOREIGN KEY(" + RENTAL_BICYCLE_ID + ") REFERENCES " + TABLE_BICYCLES + "(" + BICYCLE_ID + ")" + ")";

        db.execSQL(CREATE_CUSTOMERS_TABLE);
        db.execSQL(CREATE_BICYCLES_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_RENTAL_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BICYCLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RENTAL_HISTORY);
        onCreate(db);
    }

    // Customer operations
    public long addCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_USERNAME, customer.getUsername());
        values.put(CUSTOMER_EMAIL, customer.getEmail());
        values.put(CUSTOMER_PASSWORD, customer.getPassword());
        values.put(CUSTOMER_PHONE, customer.getPhone());
        values.put(CUSTOMER_ID_NUMBER, customer.getIdNumber());
        values.put(CUSTOMER_BIRTHDAY, customer.getBirthday());
        values.put(CUSTOMER_PROFILE_PICTURE, customer.getProfilePicture());

        long id = db.insert(TABLE_CUSTOMERS, null, values);
        db.close();
        return id;
    }

    public Customer getCustomer(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CUSTOMERS, null,
                CUSTOMER_EMAIL + "=? AND " + CUSTOMER_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);

        Customer customer = null;
        if (cursor.moveToFirst()) {
            customer = new Customer();
            customer.setId(cursor.getInt(cursor.getColumnIndexOrThrow(CUSTOMER_ID)));
            customer.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_USERNAME)));
            customer.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_EMAIL)));
            customer.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_PHONE)));
            customer.setIdNumber(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_ID_NUMBER)));
            customer.setBirthday(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_BIRTHDAY)));
            customer.setProfilePicture(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_PROFILE_PICTURE)));
        }
        cursor.close();
        db.close();
        return customer;
    }

    public Customer getCustomerById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CUSTOMERS, null,
                CUSTOMER_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        Customer customer = null;
        if (cursor.moveToFirst()) {
            customer = new Customer();
            customer.setId(cursor.getInt(cursor.getColumnIndexOrThrow(CUSTOMER_ID)));
            customer.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_USERNAME)));
            customer.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_EMAIL)));
            customer.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_PHONE)));
            customer.setIdNumber(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_ID_NUMBER)));
            customer.setBirthday(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_BIRTHDAY)));
            customer.setProfilePicture(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_PROFILE_PICTURE)));
        }
        cursor.close();
        db.close();
        return customer;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CUSTOMERS, null);

        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setId(cursor.getInt(cursor.getColumnIndexOrThrow(CUSTOMER_ID)));
                customer.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_USERNAME)));
                customer.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_EMAIL)));
                customer.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_PHONE)));
                customer.setIdNumber(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_ID_NUMBER)));
                customer.setBirthday(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_BIRTHDAY)));
                customer.setProfilePicture(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_PROFILE_PICTURE)));
                customers.add(customer);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return customers;
    }

    public boolean updateCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_USERNAME, customer.getUsername());
        values.put(CUSTOMER_EMAIL, customer.getEmail());
        values.put(CUSTOMER_PHONE, customer.getPhone());
        values.put(CUSTOMER_ID_NUMBER, customer.getIdNumber());
        values.put(CUSTOMER_BIRTHDAY, customer.getBirthday());
        values.put(CUSTOMER_PROFILE_PICTURE, customer.getProfilePicture());

        int result = db.update(TABLE_CUSTOMERS, values, CUSTOMER_ID + "=?",
                new String[]{String.valueOf(customer.getId())});
        db.close();
        return result > 0;
    }

    // Bicycle operations
    public long addBicycle(Bicycle bicycle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BICYCLE_IMAGE, bicycle.getImage());
        values.put(BICYCLE_PRICE, bicycle.getPrice());
        values.put(BICYCLE_DESCRIPTION, bicycle.getDescription());

        long id = db.insert(TABLE_BICYCLES, null, values);
        db.close();
        return id;
    }

    public List<Bicycle> getAllBicycles() {
        List<Bicycle> bicycles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BICYCLES, null);

        if (cursor.moveToFirst()) {
            do {
                Bicycle bicycle = new Bicycle();
                bicycle.setId(cursor.getInt(cursor.getColumnIndexOrThrow(BICYCLE_ID)));
                bicycle.setImage(cursor.getString(cursor.getColumnIndexOrThrow(BICYCLE_IMAGE)));
                bicycle.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(BICYCLE_PRICE)));
                bicycle.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(BICYCLE_DESCRIPTION)));
                bicycles.add(bicycle);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bicycles;
    }

    // Cart operations
    public long addToCart(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CART_CUSTOMER_ID, cartItem.getCustomerId());
        values.put(CART_BICYCLE_ID, cartItem.getBicycleId());
        values.put(CART_START_DATE, cartItem.getStartDate());
        values.put(CART_END_DATE, cartItem.getEndDate());
        values.put(CART_TOTAL_PRICE, cartItem.getTotalPrice());

        long id = db.insert(TABLE_CART, null, values);
        db.close();
        return id;
    }

    public List<CartItem> getCartItems(int customerId) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT c.*, b.image, b.description FROM " + TABLE_CART + " c " +
                "INNER JOIN " + TABLE_BICYCLES + " b ON c." + CART_BICYCLE_ID + " = b." + BICYCLE_ID +
                " WHERE c." + CART_CUSTOMER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(customerId)});

        if (cursor.moveToFirst()) {
            do {
                CartItem cartItem = new CartItem();
                cartItem.setId(cursor.getInt(cursor.getColumnIndexOrThrow(CART_ID)));
                cartItem.setCustomerId(cursor.getInt(cursor.getColumnIndexOrThrow(CART_CUSTOMER_ID)));
                cartItem.setBicycleId(cursor.getInt(cursor.getColumnIndexOrThrow(CART_BICYCLE_ID)));
                cartItem.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(CART_START_DATE)));
                cartItem.setEndDate(cursor.getString(cursor.getColumnIndexOrThrow(CART_END_DATE)));
                cartItem.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(CART_TOTAL_PRICE)));
                cartItem.setBicycleImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                cartItem.setBicycleDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                cartItems.add(cartItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cartItems;
    }

    public void clearCart(int customerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, CART_CUSTOMER_ID + "=?", new String[]{String.valueOf(customerId)});
        db.close();
    }

    // Rental history operations
    public long addToRentalHistory(RentalHistory rentalHistory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RENTAL_CUSTOMER_ID, rentalHistory.getCustomerId());
        values.put(RENTAL_BICYCLE_ID, rentalHistory.getBicycleId());
        values.put(RENTAL_START_DATE, rentalHistory.getStartDate());
        values.put(RENTAL_END_DATE, rentalHistory.getEndDate());
        values.put(RENTAL_TOTAL_PRICE, rentalHistory.getTotalPrice());
        values.put(RENTAL_DATE, rentalHistory.getRentalDate());

        long id = db.insert(TABLE_RENTAL_HISTORY, null, values);
        db.close();
        return id;
    }

    public List<RentalHistory> getRentalHistory(int customerId) {
        List<RentalHistory> rentalHistories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT r.*, b.image, b.description FROM " + TABLE_RENTAL_HISTORY + " r " +
                "INNER JOIN " + TABLE_BICYCLES + " b ON r." + RENTAL_BICYCLE_ID + " = b." + BICYCLE_ID +
                " WHERE r." + RENTAL_CUSTOMER_ID + " = ? ORDER BY r." + RENTAL_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(customerId)});

        if (cursor.moveToFirst()) {
            do {
                RentalHistory rentalHistory = new RentalHistory();
                rentalHistory.setId(cursor.getInt(cursor.getColumnIndexOrThrow(RENTAL_ID)));
                rentalHistory.setCustomerId(cursor.getInt(cursor.getColumnIndexOrThrow(RENTAL_CUSTOMER_ID)));
                rentalHistory.setBicycleId(cursor.getInt(cursor.getColumnIndexOrThrow(RENTAL_BICYCLE_ID)));
                rentalHistory.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(RENTAL_START_DATE)));
                rentalHistory.setEndDate(cursor.getString(cursor.getColumnIndexOrThrow(RENTAL_END_DATE)));
                rentalHistory.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(RENTAL_TOTAL_PRICE)));
                rentalHistory.setRentalDate(cursor.getString(cursor.getColumnIndexOrThrow(RENTAL_DATE)));
                rentalHistory.setBicycleImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                rentalHistory.setBicycleDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                rentalHistories.add(rentalHistory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return rentalHistories;
    }
}