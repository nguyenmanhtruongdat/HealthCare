package com.example.healthcare;

public class Doctors {
    private String fullName, email, phoneNumber, major, about, exp, hospitalUnitl;

    public Doctors() {
    }

    public Doctors(String fullName, String email, String phoneNumber, String major, String about, String exp, String hospitalUnitl) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.major = major;
        this.about = about;
        this.exp = exp;
        this.hospitalUnitl = hospitalUnitl;
    }

    public Doctors(String fullName, String email, String phoneNumber, String major) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.major = major;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getHospitalUnitl() {
        return hospitalUnitl;
    }

    public void setHospitalUnitl(String hospitalUnitl) {
        this.hospitalUnitl = hospitalUnitl;
    }
}
