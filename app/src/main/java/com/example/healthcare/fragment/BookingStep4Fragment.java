package com.example.healthcare.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.healthcare.databinding.FragmentBookingStep4Binding;
import com.example.healthcare.model.BookingDoctorInformation;


public class BookingStep4Fragment extends Fragment {
    static BookingStep4Fragment instance;
    private FragmentBookingStep4Binding binding;

    public static BookingStep4Fragment getInstance() {
        if (instance == null) {
            instance = new BookingStep4Fragment();
        }
        return instance;
    }

    public BookingStep4Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookingStep4Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        super.onCreateView(inflater, container, savedInstanceState);

        BookingDoctorInformation information = new BookingDoctorInformation();
        String timeSlot = information.getTimeSlot();
        String date = information.getDate();

        binding.textInputLayout0.getEditText().setEnabled(false);
        binding.textInputLayout2.getEditText().setEnabled(false);
        binding.textInputLayout3.getEditText().setEnabled(false);
        binding.textInputLayout4.getEditText().setEnabled(false);
        binding.textInputLayout5.getEditText().setEnabled(false);
        binding.textInputLayout6.getEditText().setEnabled(false);
//        Log.d("step4", information.getUserName());
        if (timeSlot != null) {

            Log.d("step4", information.getTimeSlot());
            binding.timeSlot.setText(timeSlot);
        } else if (timeSlot == null) {
            Log.d("step4", "null");

        }

//        Log.d("step4", information.getDoctorEmail());
//        Log.d("step4", information.getUserName());
//        Log.d("step4", information.getUserEmail());

        binding.fullNamePatient.setText(information.getUserName());
        binding.emailPatient.setText(information.getUserEmail());
        binding.phoneNumberPatient.setText(information.getUserPhone());
        binding.dateOfBirth.setText(information.getDateOfBirth());
        binding.doctorName.setText(information.getDoctorName());
        binding.doctorEmail.setText(information.getDoctorEmail());
        binding.timeSlot.setText(information.getTimeSlot());
        // Inflate the layout for this fragment
        return view;
    }
}