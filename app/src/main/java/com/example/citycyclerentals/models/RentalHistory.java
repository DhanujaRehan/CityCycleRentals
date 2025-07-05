package com.example.citycyclerentals.models;

public class RentalHistory {
    private int id;
    private int customerId;
    private int bicycleId;
    private String startDate;
    private String endDate;
    private double totalPrice;
    private String rentalDate;
    private String bicycleImage;
    private String bicycleDescription;

    public RentalHistory() {}

    public RentalHistory(int customerId, int bicycleId, String startDate,
                         String endDate, double totalPrice, String rentalDate) {
        this.customerId = customerId;
        this.bicycleId = bicycleId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.rentalDate = rentalDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getBicycleId() { return bicycleId; }
    public void setBicycleId(int bicycleId) { this.bicycleId = bicycleId; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getRentalDate() { return rentalDate; }
    public void setRentalDate(String rentalDate) { this.rentalDate = rentalDate; }

    public String getBicycleImage() { return bicycleImage; }
    public void setBicycleImage(String bicycleImage) { this.bicycleImage = bicycleImage; }

    public String getBicycleDescription() { return bicycleDescription; }
    public void setBicycleDescription(String bicycleDescription) { this.bicycleDescription = bicycleDescription; }
}