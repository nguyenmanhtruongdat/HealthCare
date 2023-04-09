package com.example.healthcare;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityChangePasswordBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityChangePasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        binding.saveBtn.setOnClickListener(view1 -> {
            String oldPassword =binding.oldPass.getText().toString().trim();
            String newPassword =binding.newPass.getText().toString().trim();

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
                                    } else {
                                        // Password update failed
                                        Log.d(TAG, "Password update failed");
                                        Toast.makeText(ChangePasswordActivity.this, "Update password failed", Toast.LENGTH_SHORT).show();
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
    }
}