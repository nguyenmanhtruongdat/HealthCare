package com.example.healthcare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityAppointmentDetailsBinding;
import com.example.healthcare.model.BookingDoctorInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AppointmentDetailsActivity extends AppCompatActivity {
    private ActivityAppointmentDetailsBinding binding;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentDetailsBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        BookingDoctorInformation information = (BookingDoctorInformation) intent.getSerializableExtra("information");
        Log.d("details", information.getUserName());
        Log.d("details", information.getUserID());
        binding.patientName.setText(information.getUserName());
        binding.email.setText(information.getUserEmail());
        binding.phoneNumber.setText(information.getUserPhone());
        binding.appointmentDate.setText(information.getDate());
        binding.timeSlot.setText(information.getTimeSlot());

        StorageReference profileImageRef = storage.getReference().child("profile_images/" + information.getUserID() + "_avatar.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        profileImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes -> {
                    // Create a Bitmap object from the byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    // Use the bitmap object to display the image in your app
                    binding.avtProfile.setImageBitmap(bitmap);
                })
                .addOnFailureListener(exception -> {
                    // Handle any errors that occur during the download
                    binding.avtProfile.setImageResource(R.drawable.defaul_avatar);
                });

        binding.remove.setOnClickListener(view -> {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doctors").child(information.getDoctorEmail().split("@")[0])
                    .child("appointments").child("appoint_" + information.getUserID());
            ref.child("accept").setValue("reject").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(AppointmentDetailsActivity.this, "Reject this patient", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AppointmentDetailsActivity.this, AppointmentRequestActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        });

        binding.accept.setOnClickListener(view -> {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doctors").child(information.getDoctorEmail().split("@")[0])
                    .child("appointments").child("appoint_" + information.getUserID());
            ref.child("accept").setValue("accept").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(AppointmentDetailsActivity.this, "Accept this patient", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AppointmentDetailsActivity.this, AppointmentRequestActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        });
    }
}