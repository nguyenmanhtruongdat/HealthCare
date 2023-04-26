package com.example.healthcare;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityChangePasswordBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);

        String fullName = (String) getIntent().getSerializableExtra("name");
        binding.backBtn.setOnClickListener(view1 -> {
            super.onBackPressed();
        });

        binding.signOutBtn.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this, R.style.CustomDialog);
            builder.setMessage("Are you sure you want to log out?");
            builder.setCancelable(false);
            builder.setNegativeButton("No", (dialog, id) -> {
                // If the user cancels, close the dialog
                dialog.cancel();
            });
            builder.setPositiveButton("Yes", (dialog, id) -> {
                // If the user confirms, sign them out and redirect to login activity
                mAuth.signOut();
                Intent intent1 = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                startActivity(intent1);
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
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        binding.saveBtn.setOnClickListener(view1 -> {
            String oldPassword = binding.oldPass.getText().toString().trim();
            String newPassword = binding.newPass.getText().toString().trim();

            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
            user.reauthenticate(credential)
                    .addOnSuccessListener(aVoid -> {
                        // Password re-authentication succeeded, so update the password
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        // Password update succeeded
                                        Log.d(TAG, "Password updated");
                                        Toast.makeText(ChangePasswordActivity.this, "Update password successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        // Password update failed
                                        Log.d(TAG, "Password update failed");
                                        Toast.makeText(ChangePasswordActivity.this, "Update password failed, check again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Password re-authentication failed
                            Log.d(TAG, "Password re-authentication failed");
                            Toast.makeText(ChangePasswordActivity.this, "Update password failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.cancelBtn.setOnClickListener(view1 -> {
            super.onBackPressed();
        });
    }
}