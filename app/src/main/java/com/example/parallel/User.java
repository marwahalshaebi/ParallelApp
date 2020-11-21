package com.example.parallel;

public class User {

    private String fullName, email, licenseNumber;

    public User() {
    }

    public User(String fullName) {
        this.fullName = fullName;
    }

    public User(String fullName, String email, String licenseNumber) {
        this.fullName = fullName;
        this.email = email;
        this.licenseNumber = licenseNumber;

    }


    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
    public String getEmail() {
        return email;
    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
