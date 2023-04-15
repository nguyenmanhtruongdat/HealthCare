package com.example.healthcare;

import java.io.Serializable;

public class Doctors implements Serializable {
    private String fullName, email, phoneNumber, major, about, exp, hospitalUnitl, urlImg, role;

    public Doctors() {
    }



    public Doctors(String fullName, String major, String phoneNumber) {
        this.fullName=fullName;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.major=major;
        this.about=about;
        this.role= role;
    }


    public Doctors(String fullName, String email, String phoneNumber, String major, String about, String role) {
        this.fullName = fullName;
        this.about=about;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role=role;
        this.major = major;
    }

    public Doctors(String fullName,String email,String phoneNumber,String major,String role){
        this.fullName=fullName;
        this.email=email;this.phoneNumber=phoneNumber;
        this.major=major;
        this.role=role;
    }

    public Doctors(String fullName,String email,String phoneNumber,String major,String about, int i){
        this.fullName=fullName;
        this.email=email;this.phoneNumber=phoneNumber;
        this.major=major;
        this.about=about;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public Doctors(String fullName, String email, String phoneNumber, String major) {
        this.fullName=fullName;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.major=major;
        this.role=role;
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
