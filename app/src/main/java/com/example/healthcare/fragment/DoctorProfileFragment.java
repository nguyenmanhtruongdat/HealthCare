package com.example.healthcare.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthcare.ChangePasswordActivity;
import com.example.healthcare.DoctorEditProfileActivity;
import com.example.healthcare.Doctors;
import com.example.healthcare.R;
import com.example.healthcare.databinding.FragmentDoctorProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class DoctorProfileFragment extends Fragment {
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private FragmentDoctorProfileBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public DoctorProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDoctorProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Doctors");
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        DatabaseReference userRef = mDatabase.child(user.getEmail().split("@")[0]);
        StorageReference profileImageRef = storage.getReference().child("profile_images/" + user.getEmail() + "_avatar.jpg");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Extract the user's profile data and fill the respective TextViews
                String major = snapshot.child("major").getValue(String.class);
                String email =user.getEmail().toString();
                String phone = snapshot.child("phoneNumber").getValue(String.class);
                String fullName = snapshot.child("fullName").getValue(String.class);
                String about = snapshot.child("about").getValue(String.class);
                binding.doctorAbout.setText(about);
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
            // Get a reference to the user's data in the Firebase Realtime Database
            String doctorName =binding.doctorName.getText().toString();
            String emailDoctor =binding.doctorEmail.getText().toString();
            String phoneNumber =binding.doctorPhone.getText().toString();
            String major =binding.doctorMajor.getText().toString();
            String about = binding.doctorAbout.getText().toString();
            Doctors doctors = new Doctors(doctorName, emailDoctor, phoneNumber, major, about, 1);

            // Pass the user data to the EditProfileActivity
            Intent intent = new Intent(getContext(), DoctorEditProfileActivity.class);
            intent.putExtra("doctor", doctors);
            startActivity(intent);

        });
        binding.changePasswordBtn.setOnClickListener(view3 ->{
            Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        final long ONE_MEGABYTE = 1024 * 1024;
        profileImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes -> {
                    // Create a Bitmap object from the byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    // Use the bitmap object to display the image in your app
                    binding.avtProfile.setImageBitmap(bitmap);
                })
                .addOnFailureListener(exception -> {
                    binding.avtProfile.setImageResource(R.drawable.defaul_avatar);
                });

        binding.changePasswordBtn.setOnClickListener(view3 -> {
            Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
            startActivity(intent);
        });
        return view;
    }

}
