package com.example.theotokos;

public class User {
    private String fullName;
    private String phoneNumber;
    private String birthdate;
    private String gender;
    private String address;
    private String church;
    private String username;
    private String password;
    private String level;
    private String createdDate;
    private String updatedDate;
    private Degree degree;
    public String code;

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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Degree getDegree() { return degree; }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public User(String fullName, String phoneNumber, String birthdate, String gender, String address, String church, String username, String password, String level, String createdDate, String updatedDate, Degree degrees) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
        this.gender = gender;
        this.address = address;
        this.church = church;
        this.username = username;
        this.password = password;
        this.level = level;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.degree = degrees;
    }

    public User(String fullName, String phoneNumber, String birthdate, String gender, String address, String church, String username, String password, String level, Degree degrees) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
        this.gender = gender;
        this.address = address;
        this.church = church;
        this.username = username;
        this.password = password;
        this.level = level;
        this.createdDate = ""+System.currentTimeMillis();
        this.updatedDate = ""+System.currentTimeMillis();
        this.degree = degrees;
    }

}
