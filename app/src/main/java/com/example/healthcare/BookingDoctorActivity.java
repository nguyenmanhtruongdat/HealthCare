package com.example.healthcare;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.healthcare.adapter.BookingAdapter;
import com.example.healthcare.databinding.ActivityBookingDoctorBinding;

import java.util.ArrayList;
import java.util.List;

public class BookingDoctorActivity extends AppCompatActivity {
    private ActivityBookingDoctorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingDoctorBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        setupStepView();
        setColorBtn();
        binding.viewPage.setAdapter(new BookingAdapter(getSupportFragmentManager()));
        binding.viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    binding.previous.setEnabled(false);
                } else {
                    binding.previous.setEnabled(true);
                    setColorBtn();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Choose doctor");
        stepList.add("Information");
        stepList.add("Choose time and day");
        stepList.add("Confirm");
        binding.stepView.setSteps(stepList);
    }

    private void setColorBtn() {
        if (binding.next.isEnabled()) {
            binding.next.setBackgroundResource(R.color.appColor);
        } else {
            binding.next.setBackgroundResource(R.color.gray);
        }

        if (binding.previous.isEnabled()) {
            binding.previous.setBackgroundResource(R.color.appColor);
        } else {
            binding.previous.setBackgroundResource(R.color.gray);
        }
    }

}