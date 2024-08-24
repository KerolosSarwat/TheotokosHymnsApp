package com.example.theotokos;

public class User {
    public String fullName;
    public String phoneNumber;
    public String birthdate;
    public String gender;
    public String address;
    public String church;
    public String username;
    public String password;
    public String level;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String fullName, String phoneNumber, String birthdate, String gender, String address, String church, String username, String password, String level) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
        this.gender = gender;
        this.address = address;
        this.church = church;
        this.username = username;
        this.password = password;
        this.level = level;
    }
}
