package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityBookSuccessBinding;

public class BookSuccessActivity extends AppCompatActivity {
    private ActivityBookSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookSuccessBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        binding.returnHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, HomePatientActivity.class);
            startActivity(intent);
            finish();
        });
    }
}