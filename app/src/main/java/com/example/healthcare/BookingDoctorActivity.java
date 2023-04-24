package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.healthcare.adapter.BookingAdapter;
import com.example.healthcare.databinding.ActivityBookingDoctorBinding;
import com.example.healthcare.model.BookingDoctorInformation;
import com.example.healthcare.model.SharedViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class BookingDoctorActivity extends AppCompatActivity {
    private ActivityBookingDoctorBinding binding;
    private SharedViewModel sharedViewModel;
    private BookingDoctorInformation bookingDoctorInformation;
    MaterialButton myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingDoctorBinding.inflate(getLayoutInflater());
        this.getViewModelStore().clear();
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());
        Bundle args = getIntent().getBundleExtra("data");
        String doctorName = args.getString("doctorName");
        String doctorPhone = args.getString("doctorPhone");
        String doctorMajor = args.getString("doctorMajor");
        String doctorEmail = args.getString("doctorEmail");
        String imgUrl = args.getString("img");
        Log.d("BookingDoctorActivity", "Doctor name: " + doctorName);
        Log.d("BookingDoctorActivity", "Doctor phone: " + doctorPhone);
        Log.d("BookingDoctorActivity", "Doctor major: " + doctorMajor);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        bookingDoctorInformation = new BookingDoctorInformation();

        sharedViewModel.setDoctorName(doctorName);
        sharedViewModel.setDoctorPhone(doctorPhone);
        sharedViewModel.setDoctorMajor(doctorMajor);
        sharedViewModel.setDoctorEmail(doctorEmail);
        sharedViewModel.setImgUrl(imgUrl);
        bookingDoctorInformation.setDoctorName(doctorName);
        bookingDoctorInformation.setDoctorEmail(doctorEmail);

        setupStepView();
        setColorBtn();

//        BookingStep1Fragment fragment = (BookingStep1Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        if (fragment != null) {
//            myButton = fragment.getView().findViewById(R.id.changeDr);
//            // Do something with myButton
//        }
//        myButton.setOnClickListener(view -> {
//            changeDr();
//        });


        binding.previous.setOnClickListener(view -> {
            int currentStep = binding.stepView.getCurrentStep();
            if (currentStep > 0) {
                binding.stepView.go(currentStep - 1, true); // update the current step of the StepView
                binding.viewPage.setCurrentItem(currentStep - 1); // update the current item of the ViewPager
            }
        });


        binding.next.setOnClickListener(view -> {
            int currentStep = binding.stepView.getCurrentStep();
            int totalSteps = binding.stepView.getStepCount();
            if (currentStep < totalSteps - 1) {
                binding.stepView.go(currentStep + 1, true); // update the current step of the StepView
                binding.viewPage.setCurrentItem(currentStep + 1); // update the current item of the ViewPager
            }
        });

        binding.viewPage.setAdapter(new BookingAdapter(getSupportFragmentManager()));
        binding.viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    binding.previous.setEnabled(false);
                } else {
                    binding.previous.setEnabled(true);
                }
                if (position == 2) {
                    binding.next.setEnabled(false);
                } else {
                    binding.next.setEnabled(true);
                }
            }
            @Override
            public void onPageSelected(int position) {
                binding.stepView.go(position, true);
                if (position == 0) {
                    binding.previous.setEnabled(false);
                } else {
                    binding.previous.setEnabled(true);
                }
                if (position == 2) {
                    binding.next.setEnabled(false);
                } else {
                    binding.next.setEnabled(true);
                }
                setColorBtn();
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeDr() {
        Log.d("Change", "123123");
        Intent intent = new Intent(this, SearchPatActivity.class);
        startActivity(intent);
    }

    public void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Doctor");
        stepList.add("Information");
        stepList.add("Time slot");
        binding.stepView.setSteps(stepList);
    }


    private void setColorBtn() {
        int currentStep = binding.stepView.getCurrentStep();
        binding.previous.setEnabled(true);
        binding.next.setEnabled(true);
        if (currentStep == 0) {
            binding.previous.setEnabled(false);
        }
        if (currentStep == binding.stepView.getStepCount() - 1) {
            binding.next.setEnabled(false);
        }
    }

    public void onNext() {
        binding.viewPage.setCurrentItem(binding.viewPage.getCurrentItem() + 1);
    }

}