package com.example.healthcare;

import java.io.Serializable;

public class Users implements Serializable {

    String userName, fullName, age, phoneNumber, email, role;

    public Users() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Users(String userName, String fullName, String phoneNumber, String email, String role) {
        this.fullName = fullName;
        this.email=email;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.role=role;
    }

    public Users(String userName, String fullName, String phoneNumber, String email) {
        this.fullName = fullName;
        this.role=role;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}