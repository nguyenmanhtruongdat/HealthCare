package com.example.healthcare;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityMyHeathBinding;

public class MyHeathActivity extends AppCompatActivity {
    private ActivityMyHeathBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMyHeathBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
    }
}