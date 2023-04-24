package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.healthcare.adapter.AppointmentRequestAdapter;
import com.example.healthcare.databinding.ActivityAppointmentRequestBinding;
import com.example.healthcare.model.BookingDoctorInformation;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class AppointmentRequestActivity extends AppCompatActivity implements AppointmentRequestAdapter.OnItemClickListener {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ActivityAppointmentRequestBinding binding;
    AppointmentRequestAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAppointmentRequestBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        FirebaseUser user = mAuth.getCurrentUser();
        binding.listRequest.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<BookingDoctorInformation> options =
                new FirebaseRecyclerOptions.Builder<BookingDoctorInformation>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Doctors").child(user.getEmail().split("@")[0]).child("appointments").orderByChild("accept").startAt("pending").endAt("pending" + "\uf8ff"), BookingDoctorInformation.class)
                        .build();

        mainAdapter = new AppointmentRequestAdapter(options, AppointmentRequestActivity.this, user.getEmail().split("@")[0], this);
        binding.listRequest.setAdapter(mainAdapter);


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

        Intent intent = new Intent(this, AppointmentDetailsActivity.class);
        intent.putExtra("information", request);
        Log.d("Bac si: ", request.getUserEmail() + " va " + request.getUserName());
        startActivity(intent);

    }
}