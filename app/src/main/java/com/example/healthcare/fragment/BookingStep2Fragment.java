package com.example.healthcare.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.healthcare.R;


public class BookingStep2Fragment extends Fragment {
    static BookingStep2Fragment instance;

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
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_step2, container, false);
    }
}