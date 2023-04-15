package com.example.healthcare.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthcare.Doctors;
import com.example.healthcare.R;
import com.example.healthcare.databinding.FragmentBookingStep1Binding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class BookingStep1Fragment extends Fragment {
    private FragmentBookingStep1Binding binding;
    private DatabaseReference databaseReference;
    static BookingStep1Fragment instance;
    FirebaseDatabase database = FirebaseDatabase.getInstance();;
    public static BookingStep1Fragment getInstance() {
        if (instance == null) {
            instance = new BookingStep1Fragment();
        }
        return instance;
    }
    public BookingStep1Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        binding= FragmentBookingStep1Binding.inflate(getLayoutInflater());


        databaseReference = database.getReference("Doctors");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Doctors> doctors = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Doctors doctor = snapshot.getValue(Doctors.class);
                    doctors.add(doctor);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Doctors doctors = (Doctors) getActivity().getIntent().getSerializableExtra("doctor");



        Log.d("Check dr:  ", doctors.getFullName().toString()+ doctors.getEmail());
        super.onCreate(savedInstanceState);

    }
//    private void displayDoctors(List<Doctors> doctorList) {
//        RecyclerView recyclerView = findViewById(R.id.doctorRecyclerView);
//        BookingAdapter adapter = new BookingAdapter(doctorList);
//        recyclerView.setAdapter(adapter);
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_step1, container, false);
    }
}