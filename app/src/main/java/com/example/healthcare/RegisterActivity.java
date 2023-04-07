package com.example.healthcare;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.healthcare.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
   final  String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    final String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        final String[] pw = {null};

        mAuth = FirebaseAuth.getInstance();
        binding.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String usernameInput = charSequence.toString();
                //if (usernameInput.length()>=8){
                    Pattern pattern = Pattern.compile(emailRegex);
                    Matcher matcher =pattern.matcher(usernameInput);
                    boolean usernameMatch = matcher.find();
                    if (usernameMatch)
                    {
                        binding.textInputLayout1.setHelperTextEnabled(true);
                        Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_check_circle_24);
                        tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                        ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                        SpannableString spannableString = new SpannableString("  Valid email");
                        spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        binding.textInputLayout1.setHelperText(spannableString);
                        binding.textInputLayout1.setError("");
                        binding.textInputLayout1.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(RegisterActivity.this, R.color.tickColor)));

                    }else{

                        binding.textInputLayout1.setHelperTextEnabled(true);
                        Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_error_24);
                        tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                        ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                        SpannableString spannableString = new SpannableString("  Please enter a valid email");
                        spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        binding.textInputLayout1.setError(spannableString);
                        binding.textInputLayout1.setErrorTextColor(ColorStateList.valueOf(ContextCompat.getColor(RegisterActivity.this, R.color.errorColor)));


                    }
                //}
//                else {
//                    binding.textInputLayout1.setError("Email not valid");
//                   binding.textInputLayout1.setError("");
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String passwordInput = charSequence.toString();

                pw[0] = charSequence.toString();
                if (passwordInput.length()>=8){
                    Pattern pattern = Pattern.compile(passwordRegex);
                    Matcher matcher =pattern.matcher(passwordInput);
                    boolean passwordMatch = matcher.find();
                    if (passwordMatch)
                    {
                        binding.textInputLayout2.setHelperTextEnabled(true);
                        Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_check_circle_24);
                        tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                        ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                        SpannableString spannableString = new SpannableString("  Strong password");
                        spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        binding.textInputLayout2.setHelperText(spannableString);
                        binding.textInputLayout2.setError("");
                        binding.textInputLayout2.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(RegisterActivity.this, R.color.tickColor)));
                    }else{
                        binding.textInputLayout2.setHelperTextEnabled(true);
                        Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_error_24);
                        tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                        ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                        SpannableString spannableString = new SpannableString("  Password must be at least 8 characters long,at least one uppercase and lower letter and one digit and special character.");
                        spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        binding.textInputLayout2.setError(spannableString);
                        binding.textInputLayout2.setErrorTextColor(ColorStateList.valueOf(ContextCompat.getColor(RegisterActivity.this, R.color.errorColor)));

//                        binding.textInputLayout2.setError("Password must be at least 8 characters long,at least one uppercase and lower letter and one digit and special character.");
                    }
                }else {

                    binding.textInputLayout2.setHelperTextEnabled(true);
                    Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_error_24);
                    tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                    ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                    SpannableString spannableString = new SpannableString("  Password must be at least 8 characters");
                    spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    binding.textInputLayout2.setError(spannableString);
                    binding.textInputLayout2.setErrorTextColor(ColorStateList.valueOf(ContextCompat.getColor(RegisterActivity.this, R.color.errorColor)));


//                    binding.textInputLayout2.setHelperText("Password must be at least 8 characters");
//                    binding.textInputLayout2.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.cfPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String cfPassword = charSequence.toString();
                String passwordInput = binding.password.toString();
                Log.e("concu","Pass"+cfPassword);
                Log.e("concu","confirm pass:" +pw[0].toString());

                boolean match = false;
//                if (cfPassword.length()>=8){
                if (pw[0].equals(cfPassword)){
                    match=true;
                }
                    if (match)
                    {


                        binding.textInputLayout3.setHelperTextEnabled(true);
                        Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_check_circle_24);
                        tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                        ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                        SpannableString spannableString = new SpannableString("  Password match");
                        spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        binding.textInputLayout3.setHelperText(spannableString);
                        binding.textInputLayout3.setError("");
                        binding.textInputLayout3.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(RegisterActivity.this, R.color.tickColor)));
                    }else{
                        binding.textInputLayout3.setHelperTextEnabled(true);
                        Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_error_24);
                        tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                        ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                        SpannableString spannableString = new SpannableString("  Password not match");
                        spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        binding.textInputLayout3.setError(spannableString);
                        binding.textInputLayout3.setErrorTextColor(ColorStateList.valueOf(ContextCompat.getColor(RegisterActivity.this, R.color.errorColor)));
                    }
//                }else {
//                    binding.textInputLayout3.setError("Password must be 8 characters");
//                    binding.textInputLayout3.setError("");
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


binding.registerBtn.setOnClickListener(view1 -> mAuth.createUserWithEmailAndPassword(binding.email.getText().toString().trim(), binding.password.getText().toString().trim()).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        Log.e("test",binding.email.getText().toString().trim());
        Log.e("test",binding.password.getText().toString().trim());
        Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
        if (!task.isSuccessful()) {
            Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                    Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }
    }
}));

    }


}
