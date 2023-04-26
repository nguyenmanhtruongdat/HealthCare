package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityBmiresultBinding;

public class BMIresult extends AppCompatActivity {
    private ActivityBmiresultBinding binding;
    String age;
    String weight;
    String typeUser;
    String height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBmiresultBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        age = intent.getStringExtra("age");
        weight = intent.getStringExtra("weight");
        height = intent.getStringExtra("height");
        Float intWeight = Float.parseFloat(weight);
        Float inHeight = Float.parseFloat(height);
        inHeight = inHeight / 100;
        Float bmi = intWeight / (inHeight * inHeight);
        String bmiResult = Float.toString(bmi);

        if (bmi < 16) {
            binding.bmiCategory.setText("Severe thinness");
        } else if (bmi < 16.9 && bmi > 16) {
            binding.bmiCategory.setText("Moderate thinness");
        } else if (bmi < 18.4 && bmi > 17) {
            binding.bmiCategory.setText("Mild thinness");
        } else if (bmi < 25 && bmi > 18.4) {
            binding.bmiCategory.setText("Normal");
        } else if (bmi < 29.4 && bmi > 25) {
            binding.bmiCategory.setText("Overweight");
        } else {
            binding.bmiCategory.setText("Obese Class I");
        }
        binding.bmiDisplay.setText(bmiResult);

        binding.reCalculateBmi.setOnClickListener(view -> {
            Intent intent1 = new Intent(BMIresult.this, BMICalculator.class);
            startActivity(intent1);
        });
    }
}