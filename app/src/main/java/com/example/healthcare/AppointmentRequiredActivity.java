package com.example.healthcare;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityAppointmentRequiredBinding;

public class AppointmentRequiredActivity extends AppCompatActivity {
private ActivityAppointmentRequiredBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityAppointmentRequiredBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
    }
}