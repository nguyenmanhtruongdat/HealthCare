package com.example.healthcare;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserProfileActivity extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private boolean shouldExit = false;
    private ActivityUserProfileBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityUserProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users");
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
                String username = snapshot.child("userName").getValue(String.class);
                String email =user.getEmail().toString();
                String phone = snapshot.child("phoneNumber").getValue(String.class);
                String fullName = snapshot.child("fullName").getValue(String.class);
                binding.fullname.setText(fullName);
                binding.username.setText(username);
                binding.email.setText(email);
                binding.phoneNumber.setText(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database read error
                Log.w(TAG, "getUser:onCancelled", error.toException());
            }
        });

        binding.backBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(this, HomePatientActivity.class);
            startActivity(intent);
        });

        binding.signOutBtn.setOnClickListener(view1 -> {
            // Create a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this, R.style.CustomDialog);
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
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                startActivity(intent);
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
        binding.editProfileBtn.setOnClickListener(view2 -> {
            // Get a reference to the user's data in the Firebase Realtime Database
            Users users = new Users(binding.username.getText().toString(),binding.fullname.getText().toString(),binding.phoneNumber.getText().toString(),binding.email.getText().toString());

            // Pass the user data to the EditProfileActivity
            Intent intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("user", users);
            startActivity(intent);
            finish();

    });
        binding.changePasswordBtn.setOnClickListener(view3 ->{
                    Intent intent = new Intent(UserProfileActivity.this, ChangePasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
                );
}
    @Override
    public void onBackPressed() {
        if (shouldExit) { // shouldExit is a boolean flag to determine if the app should exit or not
            super.onBackPressed();
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }


}