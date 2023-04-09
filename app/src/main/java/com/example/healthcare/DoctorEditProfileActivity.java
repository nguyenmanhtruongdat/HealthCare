package com.example.healthcare;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityDoctorEditProfileBinding;

public class DoctorEditProfileActivity extends AppCompatActivity {
    private ActivityDoctorEditProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDoctorEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}