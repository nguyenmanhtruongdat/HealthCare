package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityPostHomeBinding;

public class PostHomeActivity extends AppCompatActivity {
    private ActivityPostHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityPostHomeBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        binding.createPost.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreatePostActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
        binding.managePost.setOnClickListener(view -> {
            Intent intent = new Intent(this, ManagePostActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        binding.backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, DoctorHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

    }
}