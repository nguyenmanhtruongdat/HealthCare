package com.example.healthcare.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthcare.BookingDoctorActivity;
import com.example.healthcare.R;
import com.example.healthcare.databinding.FragmentBookingStep2Binding;
import com.example.healthcare.model.BookingDoctorInformation;
import com.example.healthcare.model.SharedViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class BookingStep2Fragment extends Fragment {
    static BookingStep2Fragment instance;
    private FragmentBookingStep2Binding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static BookingStep2Fragment getInstance() {
        if (instance == null) {
            instance = new BookingStep2Fragment();
        }
        return instance;
    }


    public BookingStep2Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        binding = FragmentBookingStep2Binding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBookingStep2Binding.inflate(inflater, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        binding.dateOfBirth.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            // Create date picker dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.MyDatePickerDialogTheme,
                    (datePicker, year, month, day) -> {
                        // Set selected date to text input field
                        String selectedDate = day + "/" + (month + 1) + "/" + year; // Note that month starts from 0
                        binding.dateOfBirth.setText(selectedDate);
                    },
                    currentYear, currentMonth, currentDay);

            // Show date picker dialog
            datePickerDialog.show();
        });

        DatabaseReference bookingsRef = database.getReference("Bookings");
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


              binding.send.setOnClickListener(view -> {
                  BookingDoctorInformation information = new ViewModelProvider(this).get(BookingDoctorInformation.class);
                  information.setDoctorEmail(sharedViewModel.getDoctorEmail().getValue());
                  information.setAccept(0);
                  information.setDoctorMajor(sharedViewModel.getDoctorMajor().getValue());
                  information.setDate(binding.dateOfBirth.getText().toString());
                  information.setUserEmail(binding.email.getText().toString());
                  Log.d("Step2", binding.email.getText().toString());
                  Log.d("Step2", binding.dateOfBirth.getText().toString());
            //bookingsRef.push().setValue(infor);
            bookingsRef.child(FirebaseAuth.getInstance().getUid()).setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                        ((BookingDoctorActivity) getActivity()).onNext();

                    }
                    if (!task.isSuccessful()) {
                        Toast.makeText(getContext(), "Updated Failed", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        });
        return binding.getRoot();
    }
}