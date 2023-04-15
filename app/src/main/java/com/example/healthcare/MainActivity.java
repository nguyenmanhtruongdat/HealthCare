package com.example.healthcare;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityLoginBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
    }
}