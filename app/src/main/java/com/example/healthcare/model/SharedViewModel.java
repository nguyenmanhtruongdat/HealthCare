package com.example.healthcare.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> userEmail = new MutableLiveData<>();
    private final MutableLiveData<String> date = new MutableLiveData<>();
    private final MutableLiveData<String> time = new MutableLiveData<>();
    private final MutableLiveData<Integer> accept = new MutableLiveData<Integer>();
    private final MutableLiveData<String> doctorName = new MutableLiveData<>();
    private final MutableLiveData<String> doctorPhone = new MutableLiveData<>();
    private final MutableLiveData<String> doctorMajor = new MutableLiveData<>();
    private final MutableLiveData<String> doctorEmail = new MutableLiveData<>();

    private final MutableLiveData<String> imgUrl = new MutableLiveData<>();

    public MutableLiveData<String> getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String email) {
        userEmail.setValue(email);
    }


    public MutableLiveData<String> getDate() {
        return date;
    }
    public void setDate(String date1) {
        date.setValue(date1);
    }


    public MutableLiveData<String> getTime() {
        return time;
    }
    public void setTime(String time1) {
        time.setValue(time1);
    }


    public MutableLiveData<Integer> getAccept() {
        return accept;
    }
    public void setAccept(int accept1) {
        accept.setValue(accept1);
    }


    public void setDoctorEmail(String name) {
        doctorEmail.setValue(name);
    }

    public LiveData<String> getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorName(String name) {
        doctorName.setValue(name);
    }

    public LiveData<String> getDoctorName() {
        return doctorName;
    }

    public void setDoctorPhone(String phone) {
        doctorPhone.setValue(phone);
    }

    public LiveData<String> getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorMajor(String major) {
        doctorMajor.setValue(major);
    }

    public LiveData<String> getDoctorMajor() {
        return doctorMajor;
    }

    public void setImgUrl(String name) {
        imgUrl.setValue(name);
    }

    public LiveData<String> getImgUrl() {
        return imgUrl;
    }
}
