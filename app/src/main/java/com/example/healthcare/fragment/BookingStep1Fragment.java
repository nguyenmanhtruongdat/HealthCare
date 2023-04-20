package com.example.healthcare.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthcare.databinding.FragmentBookingStep1Binding;
import com.example.healthcare.model.SharedViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class BookingStep1Fragment extends Fragment {
    private FragmentBookingStep1Binding binding;
    private DatabaseReference databaseReference;
    static BookingStep1Fragment instance;
    private SharedViewModel sharedViewModel;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ;

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
        binding = FragmentBookingStep1Binding.inflate(getLayoutInflater());

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
        // Inflate the layout for this fragment
        binding = FragmentBookingStep1Binding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        String doctorName = sharedViewModel.getDoctorName().getValue();
        String doctorPhone = sharedViewModel.getDoctorPhone().getValue();
        String doctorMajor = sharedViewModel.getDoctorMajor().getValue();
        String doctorEmail = sharedViewModel.getDoctorEmail().getValue();
        String imgUrl = sharedViewModel.getImgUrl().getValue();
        Log.d("BookingStep1Fragment123", "Doctor name: " + doctorName);
        Log.d("BookingStep1Fragment123", "Doctor phone: " + doctorPhone);
        Log.d("BookingStep1Fragment123", "Doctor major: " + doctorMajor);
        binding.email.setText(doctorEmail);
        Picasso.get().load(imgUrl).into(binding.avtProfile);
        binding.fullName.setText(doctorName);
        binding.phoneNumber.setText(doctorPhone);
        binding.major.setText(doctorMajor);


        return binding.getRoot();
    }

}