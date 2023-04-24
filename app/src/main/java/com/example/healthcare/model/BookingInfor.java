package com.example.healthcare.model;

public class BookingInfor {
    private String userEmail;
    private String userName;
    private String userPhone;
    private String doctorEmail;
    private String dateOfBirth;
    private String date;
    private String timeSlot;
    private String accept;
    private String doctorName;
    private String doctorMajor;

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

    public String getAccept() {
        return accept;
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

    public BookingInfor() {
        // Required empty constructor for Firebase
    }

    public BookingInfor(String userEmail, String userName, String userPhone, String doctorEmail, String dateOfBirth, String date, String timeSlot, String accept, String doctorName, String doctorMajor) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPhone = userPhone;
        this.doctorEmail = doctorEmail;
        this.dateOfBirth = dateOfBirth;
        this.date = date;
        this.timeSlot = timeSlot;
        this.accept = accept;
        this.doctorName = doctorName;
        this.doctorMajor = doctorMajor;
    }
}
