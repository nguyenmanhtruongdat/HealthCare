package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityBmicalculatorBinding;

public class BMICalculator extends AppCompatActivity {
    private ActivityBmicalculatorBinding binding;
    String typeUser = "";
    String height = "";
    String weight = "";
    boolean isFemaleClicked = false;
    boolean isMaleClicked = false;

    int intAge = 0;
    int intWeight = 0;
    int currentProgress;
    String mintProgress;
    String age = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityBmicalculatorBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());


        binding.currentWeight.setText("0");
        binding.currentAge.setText("0");
        binding.seekbar.setMax(300);
        binding.seekbar.setProgress(170);
        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                currentProgress = i;
                mintProgress = String.valueOf(currentProgress);
                binding.currentHeight.setText(mintProgress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        binding.incrementAge.setOnClickListener(view -> {
            intAge = intAge + 1;
            age = String.valueOf(intAge);
            binding.currentAge.setText(age);
        });
        binding.decrementAge.setOnClickListener(view -> {
            intAge = intAge - 1;
            age = String.valueOf(intAge);
            binding.currentAge.setText(age);
        });

        binding.decrementWeight.setOnClickListener(view -> {
            intWeight = intWeight - 1;
            age = String.valueOf(intWeight);
            binding.currentWeight.setText(age);
        });

        binding.incrementWeight.setOnClickListener(view -> {
            intWeight = intWeight + 1;
            age = String.valueOf(intWeight);
            binding.currentWeight.setText(age);
        });
        binding.calculateBmi.setOnClickListener(view -> {
            if (mintProgress.equals("")) {
                Toast.makeText(this, "Select height", Toast.LENGTH_SHORT).show();
            } else if (intAge == 0 || intAge < 0) {
                Toast.makeText(this, "Age not valid", Toast.LENGTH_SHORT).show();
            } else if (intWeight == 0 || intWeight < 0) {
                Toast.makeText(this, "Weight not valid", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(BMICalculator.this, BMIresult.class);
                intent.putExtra("height", binding.currentHeight.getText());
                intent.putExtra("weight", binding.currentWeight.getText());
                intent.putExtra("age", binding.currentAge.getText());
                startActivity(intent);
            }
        });

    }
}