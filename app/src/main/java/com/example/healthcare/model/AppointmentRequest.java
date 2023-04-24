package com.example.healthcare.model;

import java.io.Serializable;

public class AppointmentRequest implements Serializable {

    private String patientName;
    private String appointmentTime;
    private String doctorEmail;

    public AppointmentRequest() {
        // Default constructor required for calls to DataSnapshot.getValue(AppointmentRequest.class)
    }

    public AppointmentRequest(String patientName, String appointmentTime, String doctorEmail) {
        this.patientName = patientName;
        this.appointmentTime = appointmentTime;
        this.doctorEmail = doctorEmail;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }
}
