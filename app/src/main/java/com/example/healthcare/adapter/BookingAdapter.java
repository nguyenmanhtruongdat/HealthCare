package com.example.healthcare.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.healthcare.fragment.BookingStep1Fragment;
import com.example.healthcare.fragment.BookingStep2Fragment;
import com.example.healthcare.fragment.BookingStep3Fragment;
import com.example.healthcare.fragment.BookingStep4Fragment;

public class BookingAdapter extends FragmentPagerAdapter {
    public BookingAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return BookingStep1Fragment.getInstance();
            case 1: return BookingStep2Fragment.getInstance();
            case 2: return BookingStep3Fragment.getInstance();
            case 3: return BookingStep4Fragment.getInstance();
        }
        return null;
    }
    public interface OnDoctorSelectedListener {
        void onDoctorSelected(String doctorName, String doctorPhone, String doctorMajor);
    }


    @Override
    public int getCount() {
        return 3;
    }
}
