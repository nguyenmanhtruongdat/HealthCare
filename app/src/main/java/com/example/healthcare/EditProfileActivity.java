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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;

public class EditProfileActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private ActivityEditProfileBinding binding;
    StorageReference profileImageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    Uri imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);
        String uid = FirebaseAuth.getInstance().getUid();
        Users user = (Users) getIntent().getSerializableExtra("user");
        Log.d("Check: ", "username: " + user.getUserName().toString());
        Log.d("Check: ", "fullname: " + user.getFullName().toString());
        Log.d("Check: ", "email:" + user.getEmail().toString());
        Log.d("Check: ", "phone number: " + user.getPhoneNumber().toString());

        profileImageRef = storage.getReference().child("profile_images/" + uid + "_avatar.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        profileImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes -> {
                    // Create a Bitmap object from the byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    // Use the bitmap object to display the image in your app
                    binding.avtProfile.setImageBitmap(bitmap);
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        binding.avtProfile.setImageResource(R.drawable.defaul_avatar);
                    }
                });


        binding.fullname.setText(user.getFullName());
        binding.fullameed.setText(user.getFullName());
        binding.username.setText(user.getUserName());
        binding.email.setText(user.getEmail());
        binding.phoneNumber.setText(user.getPhoneNumber());


        binding.saveBtn.setOnClickListener(view1 -> {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String fullname = binding.fullameed.getText().toString().trim();
            String username = binding.username.getText().toString().trim();
            String email = binding.email.getText().toString().trim();
            String phone = binding.phoneNumber.getText().toString().trim();
            SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

            String userId = FirebaseAuth.getInstance().getUid();
            profileImageRef = storage.getReference().child("profile_images/" + userId + "_avatar.jpg");

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

            Users updatedUser = new Users(username, fullname, phone, email, "user");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            databaseReference.setValue(updatedUser).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.cancelBtn.setOnClickListener(view3 -> {
            finish();
        });
        binding.backBtn.setOnClickListener(view4 -> {
            finish();
        });


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}