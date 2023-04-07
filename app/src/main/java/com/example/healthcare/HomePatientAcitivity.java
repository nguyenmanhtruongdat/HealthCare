package com.example.healthcare;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityHomePatientAcitivityBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePatientAcitivity extends AppCompatActivity {
    private ActivityHomePatientAcitivityBinding binding;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePatientAcitivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            binding.welcome.setText("Welcome " + email + "!");
        }
    }
}
