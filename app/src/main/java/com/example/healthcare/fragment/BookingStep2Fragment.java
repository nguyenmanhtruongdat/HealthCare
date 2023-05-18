package com.example.healthcare.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthcare.BookingDoctorActivity;
import com.example.healthcare.R;
import com.example.healthcare.databinding.FragmentBookingStep2Binding;
import com.example.healthcare.model.BookingDoctorInformation;
import com.example.healthcare.model.SharedViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;


public class BookingStep2Fragment extends Fragment {
    static BookingStep2Fragment instance;
    private FragmentBookingStep2Binding binding;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = currentUser.getUid();


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    StorageReference profileImageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
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
                        String selectedDate = day + "/" + "0" + (month + 1) + "/" + year; // Note that month starts from 0
                        binding.dateOfBirth.setText(selectedDate);
                    },
                    currentYear, currentMonth, currentDay);

            // Set custom style for buttons
            datePickerDialog.show();
            datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", datePickerDialog);
            datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", datePickerDialog);
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));

            // Show date picker dialog
        });


        DatabaseReference bookingsRef = database.getReference("Bookings");
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        DatabaseReference doctorRef = database.getReference("Doctors");


        binding.send.setOnClickListener(view -> {

            String email = sharedViewModel.getDoctorEmail().getValue();
            String accept = "pending";
            String drMajor = sharedViewModel.getDoctorMajor().getValue();
            String drName = sharedViewModel.getDoctorName().getValue();
            String dob = binding.dateOfBirth.getText().toString();
            String userEmail = binding.email.getText().toString();
            String userName = binding.fullname.getText().toString();
            String userPhone = binding.phoneNumber.getText().toString();



            BookingDoctorInformation information = new BookingDoctorInformation(userName, userEmail, userPhone, dob, drName, email, drMajor, "", "",accept, userId);
            information.setUserName(binding.fullname.getText().toString());
            information.setUserEmail(binding.email.getText().toString());
            information.setUserPhone(binding.phoneNumber.getText().toString());
            information.setDateOfBirth(binding.dateOfBirth.getText().toString());
            information.setDoctorName(drName);
            information.setDoctorEmail(email);
            information.setDoctorMajor(drMajor);
            information.setUserID(userId);

            Log.d("Step2", information.getDoctorEmail());
            Log.d("Step2", binding.dateOfBirth.getText().toString());
            String drEmail = information.getDoctorEmail().split("@")[0];



            doctorRef.child(drEmail).child("appointments").child("appoint_"+FirebaseAuth.getInstance().getUid()).setValue(information).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("appointments", "Add appointment");

                }
                if (!task.isSuccessful()) {

                }
            });


            bookingsRef.child(FirebaseAuth.getInstance().getUid()).child("appoint_"+drEmail).setValue(information).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
//                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    ((BookingDoctorActivity) getActivity()).onNext();

                }
                if (!task.isSuccessful()) {
//                        Toast.makeText(getContext(), "Updated Failed", Toast.LENGTH_SHORT).show();

                }
            });
        });
        return binding.getRoot();
    }
}