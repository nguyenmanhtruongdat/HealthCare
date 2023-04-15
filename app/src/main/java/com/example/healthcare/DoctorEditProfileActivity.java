package com.example.healthcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityDoctorEditProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DoctorEditProfileActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    StorageReference profileImageRef;
    FirebaseAuth auth;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private ActivityDoctorEditProfileBinding binding;
    Uri imgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDoctorEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);


        String uid = FirebaseAuth.getInstance().getUid();
        Doctors doctors= (Doctors) getIntent().getSerializableExtra("doctor");
        Log.d("Check: ", "fullname: " + doctors.getFullName().toString());
        Log.d("Check: ", "email:" + doctors.getEmail().toString());
//        Log.d("Check: ", "about: " + doctors.getAbout().toString());
        Log.d("Check: ", "phone number: " + doctors.getPhoneNumber().toString());

        profileImageRef = storage.getReference().child("profile_images/" + doctors.getEmail() + "_avatar.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        profileImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes -> {
                    // Create a Bitmap object from the byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    // Use the bitmap object to display the image in your app
                    binding.avtProfile.setImageBitmap(bitmap);
                })
                .addOnFailureListener(exception -> {
                    binding.avtProfile.setImageResource(R.drawable.defaul_avatar_dr);
                });

        binding.fullname.setText(doctors.getFullName());
        binding.fullameed.setText(doctors.getFullName());
        binding.phoneNumber.setText(doctors.getPhoneNumber());
        binding.email.setText(doctors.getEmail());
        binding.major.setText(doctors.getMajor());
        binding.about.setText(doctors.getAbout());
        binding.saveBtn.setOnClickListener(view1 -> {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            String userId = FirebaseAuth.getInstance().getUid();
            String[] parts = doctors.getEmail().split("@");
            String username = parts[0];
            profileImageRef = storage.getReference().child("profile_images/" + doctors.getEmail() + "_avatar.jpg");

            if (imgUrl != null) {
                profileImageRef.putFile(imgUrl).addOnSuccessListener(taskSnapshot -> {
                    binding.avtProfile.setImageURI(null);
                    Log.d("image", "thanh cong");
                    if (!progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(e -> {
                    Log.d("image", "that bai roi");
                    if (!progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                });
            } else {
                if (!progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
            String fullName = binding.fullameed.getText().toString().trim();
            String major = binding.major.getText().toString().trim();
            String email = binding.email.getText().toString().trim();
            String phoneNumber = binding.phoneNumber.getText().toString().trim();
            String about= binding.about.getText().toString().trim();
            Doctors updatedDoctors = new Doctors(fullName, email, phoneNumber, major, about, "doctor");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Doctors").child(userId);
            databaseReference.setValue(updatedDoctors).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(DoctorEditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DoctorEditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.cancelBtn.setOnClickListener(view3->{
            Intent intent = new Intent(DoctorEditProfileActivity.this, DoctorProfileActivity.class);
            startActivity(intent);
            finish();
        });
        binding.backBtn.setOnClickListener(view4 ->{
            Intent intent = new Intent(DoctorEditProfileActivity.this, DoctorProfileActivity.class);
            startActivity(intent);
            finish();
        } );
        binding.avtProfile.setOnClickListener(view2 -> {
                    selectImage();
                }
        );
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imgUrl = data.getData();
            binding.avtProfile.setImageURI(imgUrl);
        }
    }
}