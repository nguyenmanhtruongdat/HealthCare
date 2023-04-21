package com.example.healthcare.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.R;
import com.example.healthcare.model.TimeSlot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    private TimeSlotClickListener listener;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public TimeSlotAdapter(List<TimeSlot> timeSlots, Context context, String doctorEmail, DatabaseReference databaseReference, String date, TimeSlotClickListener listener) {
        this.timeSlots = timeSlots;
        this.context = context;
        this.date = date;
        this.doctorEmail = doctorEmail;
        this.listener = listener;
        this.mDatabaseReference = databaseReference;
    }


    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_slot_item, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        TimeSlot timeSlot = timeSlots.get(position);
        holder.timeTextView.setText(timeSlot.getStartTime());

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
                                    holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.time_slot_item_bg));

                                } else {
                                    holder.availableTextView.setText("Full");
                                    holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.tim_slot_item_full_bg));

                                }
                            } else {
                                holder.availableTextView.setText("Unknown");
                                holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.time_slot_item_unknown_bg));

                            }
                        } else if (value instanceof HashMap) {
                            // Retrieve the appropriate value from the HashMap
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) value;
                            Object availableObject = hashMap.get("available");
                            if (availableObject instanceof Boolean) {
                                Boolean available = (Boolean) availableObject;
                                if (available != null) {
                                    if (available) {
                                        holder.availableTextView.setText("Available");
                                    } else {
                                        holder.availableTextView.setText("Full");
                                    }
                                } else {
                                    holder.availableTextView.setText("Unknown");
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
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        private TextView timeTextView;
        private TextView availableTextView;


        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            availableTextView = itemView.findViewById(R.id.timeTextAvailable);

        }
    }

    public interface TimeSlotClickListener {
        void onTimeSlotClicked(TimeSlot timeSlot);
    }
}
