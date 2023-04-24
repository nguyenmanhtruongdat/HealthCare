package com.example.healthcare.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.BookSuccessActivity;
import com.example.healthcare.R;
import com.example.healthcare.model.BookingDoctorInformation;
import com.example.healthcare.model.SharedViewModel;
import com.example.healthcare.model.TimeSlot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {
    private DatabaseReference mDatabaseReference;
    private String doctorEmail;
    private String date;
    private List<TimeSlot> timeSlots;
    private Context context;
    String email;
    private TimeSlot selectedTimeSlot;
    BookingDoctorInformation information;
    private TimeSlotSelectedListener timeSlotSelectedListener;
    private int selectedPosition = -1;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public TimeSlotAdapter(List<TimeSlot> timeSlots, Context context, String doctorEmail, DatabaseReference databaseReference, String date, TimeSlotSelectedListener listener) {
        this.timeSlots = timeSlots;
        this.context = context;
        this.date = date;
        this.doctorEmail = doctorEmail;
        this.timeSlotSelectedListener = listener;
        this.mDatabaseReference = databaseReference;
    }


    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_slot_item, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TimeSlot timeSlot = timeSlots.get(holder.getAdapterPosition());
        holder.timeTextView.setText(timeSlot.getStartTime());

        // Add the following line to check if the current time slot has been selected
        holder.itemView.setBackgroundColor(timeSlot.isSelected() ? ContextCompat.getColor(context, R.color.appColor) : ContextCompat.getColor(context, R.color.soft_gray));


        Log.d("timeslot1", date);
        mDatabaseReference.child("Doctors").child(doctorEmail)
                .child("availability").child(date)
                .child(timeSlot.getStartTime()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Object value = snapshot.getValue();

                        if (value instanceof Boolean) {
                            Boolean available = (Boolean) value;
                            if (available != null) {
                                if (available) {
                                    holder.availableTextView.setText("Available");
                                    holder.setTimeSlotSelectedListener(timeSlotSelectedListener);
                                } else {
                                    holder.availableTextView.setText("Full");
                                    holder.itemView.setEnabled(false);
                                    holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.errorColor));
                                }

                            } else {
                                holder.availableTextView.setText("Unknown");
                                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
                            }
                        } else if (value instanceof HashMap) {
                            // Retrieve the appropriate value from the HashMap
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) value;
                            Object availableObject = hashMap.get("available");
                            if (availableObject instanceof Boolean) {
                                Boolean available = (Boolean) availableObject;
                                if (available != null) {
                                    if (available) {
                                        holder.setTimeSlotSelectedListener(timeSlotSelectedListener);
                                        holder.availableTextView.setText("Available");
                                    } else {
                                        holder.availableTextView.setText("Full");
                                        holder.itemView.setEnabled(false);
                                        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.errorColor));
                                    }
                                } else {
                                    holder.availableTextView.setText("Unknown");
                                    holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
                                }
                            }
                        } else {
                            // handle other data types as needed
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("", "onCancelled: ", error.toException());
                    }
                });

        // Add a click listener to the itemView to toggle the selected state of the time slot
        holder.itemView.setOnClickListener(view -> {
            if (timeSlot.isAvailable()) {
                if (selectedPosition != position) {
                    // Update UI for previously selected item
                    if (selectedPosition != -1) {
                        if (selectedPosition != position) {
                            // Update UI for previously selected item
                            selectedTimeSlot = timeSlots.get(selectedPosition);
                            notifyItemChanged(selectedPosition);
                            DatabaseReference prevSlotRef = FirebaseDatabase.getInstance().getReference("Doctors").child(email).child("availability").child(date).child(selectedTimeSlot.getStartTime());
                            prevSlotRef.setValue(true);
                        }
                    }

                    // Show confirmation dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialog);
                    builder.setTitle("Confirm booking");
                    builder.setMessage("Do you want to book this time slot?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Update UI for current item
                            selectedPosition = position;
                            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.appColor));

                            // Add a log statement to check if onTimeSlotSelected method is being called
                            timeSlotSelectedListener.onTimeSlotSelected(timeSlot);

                            SharedViewModel shareViewModel = new ViewModelProvider((FragmentActivity) context).get(SharedViewModel.class);
                            information = new BookingDoctorInformation(timeSlot.getStartTime(), date);
                            information.setTimeSlot(timeSlot.getStartTime());
                            information.setDate(date);
                            BookingDoctorInformation infor = new BookingDoctorInformation();
                            infor.setDate(date);
                            infor.setTimeSlot(timeSlot.getStartTime());
                            ;
                            String[] parts = doctorEmail.split("@");
                            email = parts[0];
                            Log.d("email", email);

                            BookingDoctorInformation update = new BookingDoctorInformation(timeSlot.getStartTime());
                            DatabaseReference timeSlotRef = FirebaseDatabase.getInstance().getReference("Doctors").child(email).child("appointments").child("appoint_"+FirebaseAuth.getInstance().getUid()).child("timeSlot");
                            timeSlotRef.setValue(timeSlot.getStartTime()).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show();
                                }
                            });

                            BookingDoctorInformation update1 = new BookingDoctorInformation(date, "1");
                            DatabaseReference dateRef = FirebaseDatabase.getInstance().getReference("Doctors").child(email).child("appointments").child("appoint_"+FirebaseAuth.getInstance().getUid()).child("date");
                            dateRef.setValue(date).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show();
                                }
                            });
                            DatabaseReference timeSlotRef1 = FirebaseDatabase.getInstance().getReference("Bookings").child(FirebaseAuth.getInstance().getUid()).child("appoint_"+email);
                            ;
                            timeSlotRef1.child("timeSlot").setValue(timeSlot.getStartTime()).addOnCompleteListener(task -> {
                                // Data added successfully
                                DatabaseReference availabilityRef = FirebaseDatabase.getInstance().getReference("Doctors").child(email).child("availability");
                                availabilityRef.child(date).child(timeSlot.getStartTime()).setValue(false);
                                Toast.makeText(context, "Book appointment success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, BookSuccessActivity.class);
                                context.startActivity(intent);
                            });

                            DatabaseReference dateRef1 = FirebaseDatabase.getInstance().getReference("Bookings").child(FirebaseAuth.getInstance().getUid()).child("appoint_"+email);
                            dateRef1.child("date").setValue(date).addOnCompleteListener(task -> {
                                // Data added successfully
                                DatabaseReference availabilityRef = FirebaseDatabase.getInstance().getReference("Doctors").child(email).child("availability");
                                availabilityRef.child(date).child(timeSlot.getStartTime()).setValue(false);
                                Toast.makeText(context, "Book appointment success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, BookSuccessActivity.class);
                                context.startActivity(intent);
                            });


                            Log.d("infor", information.getTimeSlot());
                            if (timeSlotSelectedListener == null) {
                                Log.e("adapter", "slistener is null");
                                return;
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
// Get color resources from the context of the adapter
                    int buttonYesColor = ContextCompat.getColor(context, R.color.appColor);
                    int buttonNoColor = ContextCompat.getColor(context, R.color.appColor);

// Set the button text color

                    Button yesButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    Button noButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    yesButton.setTextColor(buttonYesColor);
                    noButton.setTextColor(buttonYesColor);

                } else {
                    // Deselect currently selected item
                    selectedPosition = -1;
                    DatabaseReference availabilityRef = FirebaseDatabase.getInstance().getReference("Doctors").child(email).child("availability");
                    availabilityRef.child(date).child(timeSlot.getStartTime()).setValue(true);
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.soft_gray));
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return timeSlots.size();
    }


    static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        private TextView timeTextView;
        private TextView availableTextView;
        TimeSlotSelectedListener timeSlotSelectedListener;
        private BookingDoctorInformation bookingDoctorInformation;
        LinearLayout cardView;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            availableTextView = itemView.findViewById(R.id.timeTextAvailable);
            cardView = itemView.findViewById(R.id.card_time_slot);
            cardView.setOnClickListener(view -> {
                if (timeSlotSelectedListener != null) {
                    boolean isAvailable = availableTextView.getText().toString().equals("Available");
                    timeSlotSelectedListener.onTimeSlotSelected(new TimeSlot(
                            timeTextView.getText().toString(), isAvailable
                    ));
                }
            });
        }

        public void setTimeSlotSelectedListener(TimeSlotSelectedListener listener) {
            timeSlotSelectedListener = listener;
        }
    }

    public interface TimeSlotSelectedListener {
        void onTimeSlotSelected(TimeSlot timeSlot);
    }
}
