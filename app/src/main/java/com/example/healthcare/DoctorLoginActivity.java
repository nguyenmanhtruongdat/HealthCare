package com.example.healthcare;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityDoctorLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DoctorLoginActivity extends AppCompatActivity {
    private ActivityDoctorLoginBinding binding;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDoctorLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                if (username.length()>0 && password.length()>0)
                    Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
                else                 Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();

            }
        });

        binding.loginBtn.setOnClickListener(view1 -> {
            mAuth.signInWithEmailAndPassword(binding.email.getText().toString().trim(), binding.password.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(DoctorLoginActivity.this, "signInWithEmail: "+binding.email.getText().toString().trim()+" :success", Toast.LENGTH_SHORT).show();
                                Log.d("checkLogin", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(DoctorLoginActivity.this, DoctorHomeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(DoctorLoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });

        });

        binding.register.setOnClickListener(view2 -> startActivity(
                new Intent(DoctorLoginActivity.this, DoctorRegisterActivity.class)));
        binding.forgotPass.setOnClickListener(view3 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(DoctorLoginActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
            EditText emailBox = dialogView.findViewById(R.id.emailBox);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialogView.findViewById(R.id.resetBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = emailBox.getText().toString().trim();
                    if (TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        Toast.makeText(DoctorLoginActivity.this,"Enter your email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(DoctorLoginActivity.this, "Check your email for reset link !", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }else {
                                Toast.makeText(DoctorLoginActivity.this, "Send failed", Toast.LENGTH_SHORT).show();
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
            if (dialog.getWindow()!=null){
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            dialog.show();
        });
    }
}