package com.example.healthcare;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityDoctorHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorHomeActivity extends AppCompatActivity {
    private ActivityDoctorHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDoctorHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);

        binding.signOutBtn.setOnClickListener(view1 -> {
            // Create a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(DoctorHomeActivity.this, R.style.CustomDialog);
            builder.setMessage("Are you sure you want to log out?");
            builder.setCancelable(false);
            builder.setNegativeButton("No", (dialog, id) -> {
                // If the user cancels, close the dialog
                dialog.cancel();
            });
            builder.setPositiveButton("Yes", (dialog, id) -> {
                // If the user confirms, sign them out and redirect to login activity
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(DoctorHomeActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setTitle("Log out");
            alertDialog.show();
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(getResources().getColor(R.color.dialog_button_text_color));

            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setTextColor(getResources().getColor(R.color.dialog_button_text_color));
            // Show the dialog
        });
        binding.profile.setOnClickListener(view2 -> {
            Intent intent = new Intent(DoctorHomeActivity.this, DoctorProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseDatabase.getInstance().getReference().child("Doctors").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String fullName = snapshot.child("fullName").getValue(String.class);
                        binding.welcomeText.setText("Welcome " + fullName + "!");

                        String logData = snapshot.child("logData").getValue(String.class);
                        Intent intent = new Intent(DoctorHomeActivity.this, UserProfileActivity.class);
                        intent.putExtra("logData", logData);
                        //startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(DoctorHomeActivity.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}