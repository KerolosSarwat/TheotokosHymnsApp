package com.example.theotokos;

import androidx.annotation.Keep;

@Keep
public class User {
    private String fullName;
    private String phoneNumber;
    private String birthdate;
    private String gender;
    private String address;
    private String church;
    private String level;
    private Degree degree;
    public String code;

    public User() {
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public User(String fullName, String phoneNumber, String birthdate, String gender, String address, String church, String level, Degree degrees) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
        this.gender = gender;
        this.address = address;
        this.church = church;
        this.level = level;
        this.degree = degrees;
    }

    public User(String fullName, String phoneNumber, String birthdate, String gender, String address, String church, String username, String level, Degree degrees) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
        this.gender = gender;
        this.address = address;
        this.church = church;
        this.level = level;
        this.degree = degrees;
    }

}
