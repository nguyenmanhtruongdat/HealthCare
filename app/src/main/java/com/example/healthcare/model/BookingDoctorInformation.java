package com.example.healthcare.model;

import java.io.Serializable;

public class BookingDoctorInformation implements Serializable {
    private String userName;
    private String userEmail;
    private String userPhone;
    private String dateOfBirth;
    private String doctorName;
    private String doctorEmail;
    private String doctorMajor;
    private String date;
    private String timeSlot;
    private String accept;
    private String userID;

    public String getAccept() {
        return accept;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public BookingDoctorInformation(String userName, String userEmail, String userPhone, String dateOfBirth, String doctorName, String doctorEmail, String doctorMajor, String date, String timeSlot, String accept, String userID) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.dateOfBirth = dateOfBirth;
        this.doctorName = doctorName;
        this.doctorEmail = doctorEmail;
        this.doctorMajor = doctorMajor;
        this.date = date;
        this.timeSlot = timeSlot;
        this.accept = accept;
        this.userID=userID;
    }

    public BookingDoctorInformation(String startTime) {
        this.timeSlot = startTime;
    }

    public BookingDoctorInformation(String date, String i) {
        this.date = date;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }


    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorMajor() {
        return doctorMajor;
    }

    public void setDoctorMajor(String doctorMajor) {
        this.doctorMajor = doctorMajor;
    }


    public BookingDoctorInformation() {

    }
}
