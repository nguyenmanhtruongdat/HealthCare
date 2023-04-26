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

import com.example.healthcare.adapter.MyPatientAdapter;
import com.example.healthcare.databinding.ActivityMyPatientBinding;
import com.example.healthcare.model.BookingDoctorInformation;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MyPatientActivity extends AppCompatActivity implements MyPatientAdapter.OnItemClickListener {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ActivityMyPatientBinding binding;
    MyPatientAdapter mainAdapter;
    private static final int REQUEST_CODE_APPOINTMENT_DETAILS = 1;
    private boolean shouldExit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMyPatientBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        FirebaseUser user = mAuth.getCurrentUser();
        binding.listRequest.setLayoutManager(new LinearLayoutManager(this));

        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());
        FirebaseRecyclerOptions<BookingDoctorInformation> options =
                new FirebaseRecyclerOptions.Builder<BookingDoctorInformation>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Doctors").child(user.getEmail().split("@")[0]).child("appointments").orderByChild("accept").startAt("accept").endAt("accept" + "\uf8ff"), BookingDoctorInformation.class)
                        .build();

        mainAdapter = new MyPatientAdapter(options, MyPatientActivity.this, user.getEmail().split("@")[0], this);
        binding.listRequest.setAdapter(mainAdapter);

        binding.backBtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(MyPatientActivity.this, DoctorHomeActivity.class);
            startActivity(intent1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });
        binding.signOutBtn.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyPatientActivity.this, R.style.CustomDialog);
            builder.setMessage("Are you sure you want to log out?");
            builder.setCancelable(false);
            builder.setNegativeButton("No", (dialog, id) -> {
                // If the user cancels, close the dialog
                dialog.cancel();
            });
            builder.setPositiveButton("Yes", (dialog, id) -> {
                // If the user confirms, sign them out and redirect to login activity
                mAuth.signOut();
                Intent intent1 = new Intent(MyPatientActivity.this, LoginActivity.class);
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
    public void onBackPressed() {
        if (shouldExit) { // shouldExit is a boolean flag to determine if the app should exit or not
            Intent intent1 = new Intent(MyPatientActivity.this, DoctorHomeActivity.class);
            startActivity(intent1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }


    @Override
    public void onItemClick(BookingDoctorInformation request) {

        Intent intent = new Intent(this, MyPatientDetailActivity.class);
        intent.putExtra("information", request);
        Log.d("Bac si: ", request.getUserEmail() + " va " + request.getUserName());
        startActivityForResult(intent, REQUEST_CODE_APPOINTMENT_DETAILS);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_APPOINTMENT_DETAILS && resultCode == RESULT_OK) {
            // handle the result here, if any
        }
    }


}