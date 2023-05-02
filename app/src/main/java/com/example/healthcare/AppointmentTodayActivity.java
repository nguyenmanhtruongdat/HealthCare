package com.example.healthcare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.healthcare.adapter.AppointmentTodayAdapter;
import com.example.healthcare.databinding.ActivityAppointmentTodayBinding;
import com.example.healthcare.model.BookingDoctorInformation;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AppointmentTodayActivity extends AppCompatActivity implements AppointmentTodayAdapter.OnItemClickListener {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ActivityAppointmentTodayBinding binding;
    AppointmentTodayAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAppointmentTodayBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        FirebaseUser user = mAuth.getCurrentUser();
        binding.listRequest.setLayoutManager(new LinearLayoutManager(this));

        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateString = currentDate.format(dateFormat).replace("/","-");
        Log.d("date", dateString);
        FirebaseRecyclerOptions<BookingDoctorInformation> options =
                new FirebaseRecyclerOptions.Builder<BookingDoctorInformation>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Bookings").child(user.getUid()).orderByChild("date").startAt(dateString).endAt(dateString + "\uf8ff"), BookingDoctorInformation.class)
                        .build();

        mainAdapter = new AppointmentTodayAdapter(options, AppointmentTodayActivity.this, user.getUid(), this, this);
        binding.listRequest.setAdapter(mainAdapter);

        binding.backBtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(AppointmentTodayActivity.this, HomePatientActivity.class);
            startActivity(intent1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });

        binding.signOutBtn.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentTodayActivity.this, R.style.CustomDialog);
            builder.setMessage("Are you sure you want to log out?");
            builder.setCancelable(false);
            builder.setNegativeButton("No", (dialog, id) -> {
                // If the user cancels, close the dialog
                dialog.cancel();
            });
            builder.setPositiveButton("Yes", (dialog, id) -> {
                // If the user confirms, sign them out and redirect to login activity
                mAuth.signOut();
                Intent intent1 = new Intent(AppointmentTodayActivity.this, LoginActivity.class);
                startActivity(intent1);
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
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();

    }


    @Override
    public void onItemClick(BookingDoctorInformation request) {

        Intent intent = new Intent(this, AppointmentTodayDetailActivity.class);
        intent.putExtra("information", request);
        Log.d("Bac si: ", request.getUserEmail() + " va " + request.getUserName());
        startActivity(intent);

    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}