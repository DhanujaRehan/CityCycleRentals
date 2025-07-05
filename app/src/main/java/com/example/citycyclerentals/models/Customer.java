package com.example.citycyclerentals.models;

public class Customer {
    private int id;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String idNumber;
    private String birthday;
    private String profilePicture;

    public Customer() {}

    public Customer(String username, String email, String password, String phone,
                    String idNumber, String birthday, String profilePicture) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.idNumber = idNumber;
        this.birthday = birthday;
        this.profilePicture = profilePicture;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
}