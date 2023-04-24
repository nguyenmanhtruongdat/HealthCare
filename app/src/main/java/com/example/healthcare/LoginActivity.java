package com.example.healthcare;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    boolean passwordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);
        binding.loginProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bg));
        binding.loginBtn.setOnClickListener(view1 -> {

            binding.loginProgressBar.setVisibility(View.VISIBLE);


            mAuth.signInWithEmailAndPassword(binding.email.getText().toString().trim(), binding.password.getText().toString().trim())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            binding.loginProgressBar.setVisibility(View.GONE);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                String email = user.getEmail();
                                String[] parts = email.split("@");
                                String doctorEmail = parts[0];
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                                DatabaseReference doctorsRef = FirebaseDatabase.getInstance().getReference("Doctors").child(doctorEmail);
                                ValueEventListener valueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists() && dataSnapshot.child("role").getValue() != null) {
                                            String role = dataSnapshot.child("role").getValue().toString();
                                            if (role.equals("doctor")) {
                                                Intent intent = new Intent(LoginActivity.this, DoctorHomeActivity.class);
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                finish();
                                            } else if (role.equals("user")) {
                                                Intent intent = new Intent(LoginActivity.this, HomePatientActivity.class);
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                finish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                                    }
                                };
                                usersRef.get().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful() && task1.getResult().getValue() != null) {
                                        DataSnapshot dataSnapshot = task1.getResult();
                                        valueEventListener.onDataChange(dataSnapshot);
                                    } else {
                                        doctorsRef.get().addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful() && task2.getResult().getValue() != null) {
                                                DataSnapshot dataSnapshot = task2.getResult();
                                                valueEventListener.onDataChange(dataSnapshot);
                                            }
                                        });
                                    }
                                });
                            }


                            // Sign in success, update UI with the signed-in user's information
                        } else {
                            binding.loginProgressBar.setVisibility(View.GONE);

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    });

        });


        binding.register.setOnClickListener(view2 -> startActivity(
                new Intent(LoginActivity.this, RegisterActivity.class)));

        binding.registerDr.setOnClickListener(view1 -> startActivity(new Intent(LoginActivity.this, DoctorRegisterActivity.class)));

        binding.forgotPass.setOnClickListener(view3 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
            EditText emailBox = dialogView.findViewById(R.id.emailBox);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialogView.findViewById(R.id.resetBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = emailBox.getText().toString().trim();
                    if (TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(LoginActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Check your email for reset link !", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            } else {
                                Toast.makeText(LoginActivity.this, "Send failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            dialogView.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            dialog.show();
        });
    }
}
