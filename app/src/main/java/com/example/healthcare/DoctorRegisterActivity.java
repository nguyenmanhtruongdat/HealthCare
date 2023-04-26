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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.healthcare.databinding.ActivityDoctorRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoctorRegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    final String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    final String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    final String phoneRegex = "(\\+84|0)\\d{9,10}";

    private FirebaseDatabase firebaseDatabase;
    private boolean emailCheck = false;
    private boolean phoneCheck = false;
    private boolean majorCheck = false;
    private boolean passwordCheck = false;
    private boolean cfPasswordCheck = false;
    private boolean nameCheck = false;
    private DatabaseReference databaseReference;
    private ActivityDoctorRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);
        binding.loginProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bg));
        final String[] pw = {null};
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Doctors");
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
                Matcher matcher = pattern.matcher(usernameInput);
                boolean usernameMatch = matcher.find();
                if (usernameMatch) {
                    binding.textInputLayout1.setHelperTextEnabled(true);
                    Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_check_circle_24);
                    tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                    ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                    SpannableString spannableString = new SpannableString("  Valid email");
                    emailCheck = true;
                    spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    binding.textInputLayout1.setHelperText(spannableString);
                    binding.textInputLayout1.setError("");
                    binding.textInputLayout1.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(DoctorRegisterActivity.this, R.color.tickColor)));

                } else {
                    emailCheck = false;
                    binding.textInputLayout1.setHelperTextEnabled(true);
                    Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_error_24);
                    tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                    ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                    SpannableString spannableString = new SpannableString("  Please enter a valid email");
                    spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    binding.textInputLayout1.setError(spannableString);
                    binding.textInputLayout1.setErrorTextColor(ColorStateList.valueOf(ContextCompat.getColor(DoctorRegisterActivity.this, R.color.errorColor)));


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


        binding.phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String phone = charSequence.toString();
                //if (usernameInput.length()>=8){
                Pattern pattern = Pattern.compile(phoneRegex);
                Matcher matcher = pattern.matcher(phone);
                boolean phoneMatch = matcher.find();
                if (phoneMatch) {
                    binding.textInputLayoutPhone.setHelperTextEnabled(true);
                    Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_check_circle_24);
                    tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                    ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                    SpannableString spannableString = new SpannableString("  Valid phone");
                    phoneCheck = true;
                    spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    binding.textInputLayoutPhone.setHelperText(spannableString);
                    binding.textInputLayoutPhone.setError("");
                    binding.textInputLayoutPhone.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(DoctorRegisterActivity.this, R.color.tickColor)));

                } else {
                    phoneCheck = false;
                    binding.textInputLayoutPhone.setHelperTextEnabled(true);
                    Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_error_24);
                    tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                    ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                    SpannableString spannableString = new SpannableString("  Please enter a valid phone number");
                    spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    binding.textInputLayoutPhone.setError(spannableString);
                    binding.textInputLayoutPhone.setErrorTextColor(ColorStateList.valueOf(ContextCompat.getColor(DoctorRegisterActivity.this, R.color.errorColor)));


                }
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
                if (passwordInput.length() >= 8) {
                    Pattern pattern = Pattern.compile(passwordRegex);
                    Matcher matcher = pattern.matcher(passwordInput);
                    boolean passwordMatch = matcher.find();
                    if (passwordMatch) {
                        binding.textInputLayout2.setHelperTextEnabled(true);
                        Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_check_circle_24);
                        tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                        ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                        SpannableString spannableString = new SpannableString("  Strong password");
                        passwordCheck = true;
                        spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        binding.textInputLayout2.setHelperText(spannableString);
                        binding.textInputLayout2.setError("");
                        binding.textInputLayout2.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(DoctorRegisterActivity.this, R.color.tickColor)));
                    } else {
                        binding.textInputLayout2.setHelperTextEnabled(true);
                        Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_error_24);
                        tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                        ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                        passwordCheck = false;
                        SpannableString spannableString = new SpannableString("  Password must be at least 8 characters long,at least one uppercase and lower letter and one digit and special character.");
                        spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        binding.textInputLayout2.setError(spannableString);
                        binding.textInputLayout2.setErrorTextColor(ColorStateList.valueOf(ContextCompat.getColor(DoctorRegisterActivity.this, R.color.errorColor)));

//                        binding.textInputLayout2.setError("Password must be at least 8 characters long,at least one uppercase and lower letter and one digit and special character.");
                    }
                } else {

                    binding.textInputLayout2.setHelperTextEnabled(true);
                    Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_error_24);
                    tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                    ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                    SpannableString spannableString = new SpannableString("  Password must be at least 8 characters");
                    passwordCheck = false;
                    spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    binding.textInputLayout2.setError(spannableString);
                    binding.textInputLayout2.setErrorTextColor(ColorStateList.valueOf(ContextCompat.getColor(DoctorRegisterActivity.this, R.color.errorColor)));


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
                Log.e("", "Pass" + cfPassword);
                Log.e("", "confirm pass:" + pw[0].toString());

                boolean match = false;
//                if (cfPassword.length()>=8){
                if (pw[0].equals(cfPassword)) {
                    match = true;
                }
                if (match) {


                    binding.textInputLayout3.setHelperTextEnabled(true);
                    Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_check_circle_24);
                    tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                    ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                    cfPasswordCheck = true;
                    SpannableString spannableString = new SpannableString("  Password match");
                    spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    binding.textInputLayout3.setHelperText(spannableString);
                    binding.textInputLayout3.setError("");
                    binding.textInputLayout3.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(DoctorRegisterActivity.this, R.color.tickColor)));
                } else {
                    binding.textInputLayout3.setHelperTextEnabled(true);
                    Drawable tickIcon = getResources().getDrawable(R.drawable.baseline_error_24);
                    tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
                    ImageSpan tickSpan = new ImageSpan(tickIcon, ImageSpan.ALIGN_BOTTOM);
                    SpannableString spannableString = new SpannableString("  Password not match");
                    cfPasswordCheck = false;
                    spannableString.setSpan(tickSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    binding.textInputLayout3.setError(spannableString);
                    binding.textInputLayout3.setErrorTextColor(ColorStateList.valueOf(ContextCompat.getColor(DoctorRegisterActivity.this, R.color.errorColor)));
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
        binding.registerBtn.setOnClickListener(view1 -> {
            nameCheck = !(binding.fullname.getText().equals(""));
            binding.loginProgressBar.setVisibility(View.VISIBLE);

            if (emailCheck == false || phoneCheck == false || passwordCheck == false || cfPasswordCheck == false) {
                Toast.makeText(DoctorRegisterActivity.this, "Please enter valid information", Toast.LENGTH_SHORT).show();
                binding.loginProgressBar.setVisibility(View.GONE);
                return;
            } else {
                mAuth.createUserWithEmailAndPassword(binding.email.getText().toString().trim(), binding.password.getText().toString().trim()).addOnCompleteListener(DoctorRegisterActivity.this, task -> {
                    if (task.isSuccessful()) {
                        binding.loginProgressBar.setVisibility(View.GONE);
                        String email = binding.email.getText().toString().trim();
                        String password = binding.password.getText().toString().trim();
                        String major = binding.major.getText().toString().trim();
                        String fullName = binding.fullname.getText().toString().trim();
                        String phoneNumber = binding.phoneNumber.getText().toString().trim();
                        String role = "doctor";
                        Doctors doctors = new Doctors(fullName, email, phoneNumber, major, role);
                        String[] parts = email.split("@");
                        String doctorEmail = parts[0];

//                -child username
//                -child user id
                        databaseReference.child(doctorEmail).setValue(doctors).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Doctor added successfully, add availability for the doctor
                                    DatabaseReference availabilityRef = FirebaseDatabase.getInstance().getReference("Doctors").child(doctorEmail).child("availability");
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    for (int i = 0; i < 7; i++) {
                                        calendar.add(Calendar.DATE, 1);
                                        String date = dateFormat.format(calendar.getTime());
                                        availabilityRef.child(date).setValue(getDefaultAvailability());
                                    }
                                    Toast.makeText(DoctorRegisterActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DoctorRegisterActivity.this, "Updated Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                        Log.e("test", binding.email.getText().toString().trim());
                        Log.e("test", binding.password.getText().toString().trim());
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        firebaseUser.sendEmailVerification();

                        Intent intent = new Intent(DoctorRegisterActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        Toast.makeText(DoctorRegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                    }
                    if (!task.isSuccessful()) {
                        binding.loginProgressBar.setVisibility(View.GONE);
                        Toast.makeText(DoctorRegisterActivity.this, "Register failed." + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(DoctorRegisterActivity.this, LoginActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }
                });
            }
        });

    }

    private Map<String, Map<String, Boolean>> getDefaultAvailability() {
        // Set the default availability for a day as available for all time slots
        Map<String, Map<String, Boolean>> availability = new HashMap<>();
        for (int i = 7; i < 17; i++) {
            Map<String, Boolean> timeSlot1 = new HashMap<>();
            timeSlot1.put("available", true);
            availability.put(i + ":00-" + i + ":30", timeSlot1);

            Map<String, Boolean> timeSlot2 = new HashMap<>();
            timeSlot2.put("available", true);
            availability.put(i + ":30-" + (i + 1) + ":00", timeSlot2);
        }
        return availability;
    }


}