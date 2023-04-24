package com.example.healthcare.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.R;
import com.example.healthcare.adapter.TimeSlotAdapter;
import com.example.healthcare.databinding.FragmentBookingStep3Binding;
import com.example.healthcare.model.BookingDoctorInformation;
import com.example.healthcare.model.SharedViewModel;
import com.example.healthcare.model.TimeSlot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class BookingStep3Fragment extends Fragment implements TimeSlotAdapter.TimeSlotSelectedListener {
    static BookingStep3Fragment instance;
    private RecyclerView timeSlotRecyclerView;
    private FragmentBookingStep3Binding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private List<TimeSlot> timeSlots = new ArrayList<>();
    private TimeSlot selectedTimeSlot;

    private boolean isDateSelected = false;
    private SharedViewModel sharedViewModel;

    public static BookingStep3Fragment getInstance() {
        if (instance == null) {
            instance = new BookingStep3Fragment();
        }
        return instance;
    }


    public BookingStep3Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        binding = FragmentBookingStep3Binding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BookingDoctorInformation information = new BookingDoctorInformation();

        binding = FragmentBookingStep3Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        super.onCreateView(inflater, container, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        timeSlotRecyclerView = view.findViewById(R.id.recycle_time_slot);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        timeSlotRecyclerView.setLayoutManager(layoutManager);
        timeSlots.clear();
        // Populate the list of time slots (this is just an example)
        for (int i = 7; i < 17; i++) {
            String startTime = String.format("%02d:00", i);
            String endTime = String.format("%02d:30", i);
            TimeSlot timeSlot1 = new TimeSlot(startTime + "-" + endTime, true);
            timeSlots.add(timeSlot1);

            startTime = String.format("%02d:30", i);
            endTime = String.format("%02d:00", i + 1);
            TimeSlot timeSlot2 = new TimeSlot(startTime + "-" + endTime, true);
            timeSlots.add(timeSlot2);
        }

        String email = sharedViewModel.getDoctorEmail().getValue();
        String[] parts = email.split("@");
        String doctorEmail = parts[0];
        // Log.d("step3", information.getTimeSlot().toString());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        long tomorrowInMillis = calendar.getTimeInMillis();
        Date tomorrow = calendar.getTime();
        String tomorrowDate = dateFormat.format(tomorrow);
        binding.calendar.setText(tomorrowDate);

        // Inflate the layout for this fragment
        binding.calendar.setOnClickListener(view1 -> {
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            // Create date picker dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.MyDatePickerDialogTheme,
                    (datePicker, year, month, day) -> {
                        // Set selected date to text input field
                        String selectedDate = day + "/" + "0" + (month + 1) + "/" + year; // Note that month starts from 0
                        binding.calendar.setText(selectedDate);

                        isDateSelected = true;
                        showTimeSlots();
                    },
                    currentYear, currentMonth, currentDay);

            datePickerDialog.getDatePicker().setMinDate(tomorrowInMillis);

            datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", (dialog, which) -> {
                DatePicker datePicker = datePickerDialog.getDatePicker();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int dayOfMonth = datePicker.getDayOfMonth();

                // Set selected date to text input field
                String selectedDate = dayOfMonth + "/" + "0" + (month + 1) + "/" + year; // Note that month starts from 0
                binding.calendar.setText(selectedDate);

                isDateSelected = true;
                showTimeSlots();
            });

            datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {
                // Do nothing, dialog will be dismissed
            });

            // Show date picker dialog
            datePickerDialog.show();
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        });

        TimeSlotAdapter timeSlotAdapter = new TimeSlotAdapter(timeSlots, getContext(), doctorEmail, database.getReference(), binding.calendar.getText().toString().replace('/', '-'), timeSlot -> {
            // Handle click on time slot
        });
        timeSlotRecyclerView.setAdapter(timeSlotAdapter);


        DatabaseReference bookingsRef = database.getReference("Bookings");
//        binding.send.setOnClickListener(view -> {
//            BookingDoctorInformation information = new ViewModelProvider(this).get(BookingDoctorInformation.class);
//            information.setDoctorEmail(sharedViewModel.getDoctorEmail().getValue());
//            information.setAccept(0);
//            information.setDoctorMajor(sharedViewModel.getDoctorMajor().getValue());
//            information.setDate(binding.dateOfBirth.getText().toString());
//            information.setUserEmail(binding.email.getText().toString());
//            Log.d("Step2", binding.email.getText().toString());
//            Log.d("Step2", binding.dateOfBirth.getText().toString());
//            //bookingsRef.push().setValue(infor);
//            bookingsRef.child(FirebaseAuth.getInstance().getUid()).setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
//                        ((BookingDoctorActivity) getActivity()).onNext();
//
//                    }
//                    if (!task.isSuccessful()) {
//                        Toast.makeText(getContext(), "Updated Failed", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            });
//        });
        return view;
    }

    private void showTimeSlots() {
        // Clear the existing time slots
        timeSlots.clear();

        // Generate a new list of time slots based on the selected date
        String selectedDate = binding.calendar.getText().toString().replace('/', '-');
        for (int i = 7; i < 17; i++) {
            String startTime = String.format("%02d:00", i);
            String endTime = String.format("%02d:30", i);
            TimeSlot timeSlot1 = new TimeSlot(startTime + "-" + endTime, true);
            timeSlots.add(timeSlot1);

            startTime = String.format("%02d:30", i);
            endTime = String.format("%02d:00", i + 1);
            TimeSlot timeSlot2 = new TimeSlot(startTime + "-" + endTime, true);
            timeSlots.add(timeSlot2);
        }

        // Set the new list of time slots as the data source for the recycler view adapter
        String email = sharedViewModel.getDoctorEmail().getValue();
        String[] parts = email.split("@");
        String doctorEmail = parts[0];
        TimeSlotAdapter timeSlotAdapter = new TimeSlotAdapter(timeSlots, getContext(), doctorEmail, database.getReference(), selectedDate, timeSlot -> {
            // Handle click on time slot
        });
        timeSlotRecyclerView.setAdapter(timeSlotAdapter);
    }


    @Override
    public void onTimeSlotSelected(TimeSlot timeSlot) {
        // Save the selected time slot
        selectedTimeSlot = timeSlot;

        // Update the UI to show the selected time slot
        for (TimeSlot ts : timeSlots) {
            if (ts == selectedTimeSlot) {
                ts.setSelected(true);
            } else {
                ts.setSelected(false);
            }
        }

        // Notify the adapter that the data has changed
        timeSlotRecyclerView.getAdapter().notifyDataSetChanged();

        // Log the selected time slot
        Log.d("MyApp", "Selected time slot: " + selectedTimeSlot);
    }


}