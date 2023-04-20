package com.example.healthcare;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityDoctorInformationBookBinding;
import com.example.healthcare.model.SharedViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.atomic.AtomicReference;

public class DoctorInformationBook extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private String doctorUid = "";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference databaseReference;
    private ActivityDoctorInformationBookBinding binding;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AtomicReference<Bundle> args = new AtomicReference<>(new Bundle());
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorInformationBookBinding.inflate(getLayoutInflater());

        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.signOutBtn.setOnClickListener(view1 -> {
            // Create a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(DoctorInformationBook.this, R.style.CustomDialog);
            builder.setMessage("Are you sure you want to log out?");
            builder.setCancelable(false);
            builder.setNegativeButton("No", (dialog, id) -> {
                // If the user cancels, close the dialog
                dialog.cancel();
            });
            builder.setPositiveButton("Yes", (dialog, id) -> {
                // If the user confirms, sign them out and redirect to login activity
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(DoctorInformationBook.this, LoginActivity.class);
                startActivity(intent);
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
            // Show the dialog
        });

        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase = database.getReference("Doctors");
        databaseReference = database.getReference("Doctors");
        // Get the Doctor object from the extras
        Doctors doctor = (Doctors) intent.getSerializableExtra("doctor");
        Query query = mDatabase.orderByChild("email").equalTo(doctor.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    doctorUid = childSnapshot.getKey();
                    // Use the doctorUid as needed
                    Log.d("Doctor UID: ", doctorUid);
                    // Set the database reference to the doctorUid child reference
                    databaseReference = mDatabase.child(doctorUid);
                    // Retrieve the data from the database using the new reference
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Extract the user's profile data and fill the respective TextViews
                            String major = snapshot.child("major").getValue(String.class);
                            String email = doctor.getEmail().toString();
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        StorageReference profileImageRef = storage.getReference().child("profile_images/" + doctor.getEmail() + "_avatar.jpg");
        profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();
            args.set(new Bundle());
            args.get().putString("img", imageUrl);
        });
        final long ONE_MEGABYTE = 1024 * 1024;
        profileImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            // Create a Bitmap object from the byte array
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // Use the bitmap object to display the image in your app
            binding.avtProfile.setImageBitmap(bitmap);
        }).addOnFailureListener(exception -> {
            binding.avtProfile.setImageResource(R.drawable.defaul_avatar);
        });


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Extract the user's profile data and fill the respective TextViews
                String major = snapshot.child("major").getValue(String.class);
                String email = doctor.getEmail().toString();
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

        binding.bookingDoctor.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, BookingDoctorActivity.class);

            // Put data to pass to the BookingStep1Fragment
            args.get().putString("doctorName", binding.doctorName.getText().toString());
            args.get().putString("doctorPhone", binding.doctorPhone.getText().toString());
            args.get().putString("doctorMajor", binding.doctorMajor.getText().toString());
            args.get().putString("doctorEmail", binding.doctorEmail.getText().toString());

            intent1.putExtra("data", args.get());

            Log.d("BookingDoctorActivity", "Doctor name: " + binding.doctorName.getText().toString());
            Log.d("BookingDoctorActivity", "Doctor phone: " + binding.doctorPhone.getText().toString());
            Log.d("BookingDoctorActivity", "Doctor major: " + binding.doctorMajor.getText().toString());

            // Start the BookingDoctorActivity
            startActivity(intent1);
        });


    }

    public void onBackPressed() {
        Intent intent = new Intent(this, SearchPatActivity.class);
        startActivity(intent);
        finish();
    }

}