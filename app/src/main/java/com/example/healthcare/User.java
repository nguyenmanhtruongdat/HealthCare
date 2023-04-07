package com.example.healthcare;

public class User {
    private String fullname;
    private String email;
    private String password;
    private String phoneNumber;

    public User(String fullname, String email, String password, String phoneNumber) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
