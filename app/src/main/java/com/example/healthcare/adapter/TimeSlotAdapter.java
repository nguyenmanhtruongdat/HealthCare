package com.example.healthcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.R;
import com.example.healthcare.model.Common;
import com.example.healthcare.model.TimeSlot;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.MyViewHolder> {

    Context context;
    List<TimeSlot> timeSlotList;

    public TimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_layout,parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(position)).toString());
        if (timeSlotList.size()==0){
            holder.time_slot_des.setText("Available");
            holder.time_slot_des.setTextColor(context.getResources().getColor(android.R.color.white));
            holder.time_slot.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
        }else {
            for (TimeSlot slotValue: timeSlotList){
                int slot = Integer.parseInt(slotValue.getSlot().toString());
                if (slot==position){
                holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                    holder.time_slot_des.setText("Full");
                    holder.time_slot_des.setTextColor(context.getResources().getColor(android.R.color.white));
                    holder.time_slot.setTextColor(context.getResources().getColor(android.R.color.white));

                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView time_slot, time_slot_des;
        CardView card_time_slot;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = (CardView) itemView.findViewById(R.id.card_time_slot);
            time_slot = (TextView) itemView.findViewById(R.id.time_slot);
            time_slot_des = (TextView) itemView.findViewById(R.id.time_slot_des);

        }
    }
}
