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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChurch() {
        return church;
    }

    public void setChurch(String church) {
        this.church = church;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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
