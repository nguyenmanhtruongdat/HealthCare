package com.example.healthcare;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityDoctorProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DoctorProfileActivity extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private ActivityDoctorProfileBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityDoctorProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        super.onCreate(savedInstanceState);
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Doctors");
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        DatabaseReference userRef = mDatabase.child(uid);
        StorageReference profileImageRef = storage.getReference().child("profile_images/" + uid + "_avatar.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        profileImageRef .getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes -> {
                    // Create a Bitmap object from the byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    // Use the bitmap object to display the image in your app
                    binding.avtProfile.setImageBitmap(bitmap);
                })
                .addOnFailureListener(exception -> {
                    // Handle any errors that occur during the download
                });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Extract the user's profile data and fill the respective TextViews
                String major = snapshot.child("major").getValue(String.class);
                String email =user.getEmail().toString();
                String phone = snapshot.child("phoneNumber").getValue(String.class);
                String fullName = snapshot.child("fullName").getValue(String.class);
                binding.doctorName.setText(fullName);
                binding.doctorMajor.setText(major);
                binding.doctorEmail.setText(email);
                binding.doctorPhone.setText(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database read error
                Log.w(TAG, "getUser:onCancelled", error.toException());
            }
        });

        binding.editProfileBtn.setOnClickListener(view1 -> {

        });



    }
}