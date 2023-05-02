package com.example.healthcare.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.AppointmentTodayActivity;
import com.example.healthcare.R;
import com.example.healthcare.model.BookingDoctorInformation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

public class AppointmentTodayAdapter extends FirebaseRecyclerAdapter<BookingDoctorInformation, AppointmentTodayAdapter.myViewHolder> {

    private final AppointmentTodayActivity activity;
    private Context context;
    private OnItemClickListener mListener;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference mDatabaseReference;
    private String id;

    public AppointmentTodayAdapter(@NonNull FirebaseRecyclerOptions<BookingDoctorInformation> options, AppointmentTodayActivity activity, String id, OnItemClickListener mListener, Context context) {
        super(options);
        this.context = context;
        this.activity = activity;
        this.id = id;
        this.mListener = mListener;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_today_layout, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return getSnapshots().size();
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull BookingDoctorInformation model) {

        myViewHolder viewHolder = (myViewHolder) holder;
// Get the download URL of the image

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onItemClick(model);
            }
        });


        // Cast the holder to your custom ViewHolder type

        // Get the data model for this position
        // Set values to views
        viewHolder.name.setText(model.getDoctorName());
        viewHolder.appointmentTime.setText(model.getTimeSlot());
        viewHolder.email.setText(model.getDoctorEmail());
        viewHolder.getAppointmentDate.setText(model.getDate());
        String patID = model.getUserID();
        final long ONE_MEGABYTE = 1024 * 1024;
        StorageReference profileImageRef = storage.getReference().child("profile_images/" + model.getDoctorEmail() + "_avatar.jpg");
        profileImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes -> {
                    // Create a Bitmap object from the byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    // Use the bitmap object to display the image in your app
                    viewHolder.avatar.setImageBitmap(bitmap);

                })
                .addOnFailureListener(exception -> {
                    // Handle any errors that occur during the download
                    viewHolder.avatar.setImageResource(R.drawable.defaul_avatar);
                });
//        viewHolder.email.setText(model.getUserPhone());
        if (model.getUserEmail() != null) {
            Log.d("adapter", model.getUserEmail());
        } else {
            Log.d("adapter", "null roi");

        }
        Log.d("adapter", model.getUserName());
        Log.d("adapter", model.getDoctorEmail() + model.getDate() + model.getTimeSlot());

//        holder.acceptBtn.setOnClickListener(view -> {
//            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Booking").child(id)
//                    .child("appointments").child("appoint_"+model.getUserID());
//            ref.child("accept").setValue("accept").addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()){
//                        Toast.makeText(activity, "Accept this appointment!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//
//        });


//        holder.removeBtn.setOnClickListener(view -> {
//            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doctors").child(email.split("@")[0])
//                    .child("appointments").child("appoint_"+model.getUserID());
//            ref.child("accept").setValue("reject").addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()){
//                        Toast.makeText(activity, "Reject this appointment!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        });

        // Set an image to CircularImageView using a library like Glide


        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                int itemPosition = holder.getAbsoluteAdapterPosition();
                if (itemPosition != RecyclerView.NO_POSITION) {
                    BookingDoctorInformation request = getItem(itemPosition);
                    mListener.onItemClick(request);
                }
            }
        });
    }


    public interface OnItemClickListener {
        void onItemClick(BookingDoctorInformation request);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView name, appointmentTime, getAppointmentDate, email, status;
        MaterialButton acceptBtn, removeBtn;
        CircularImageView avatar;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.doctorName);
            email = itemView.findViewById(R.id.doctorEmaila); // corrected field name
            appointmentTime = itemView.findViewById(R.id.apointmentTime);
            getAppointmentDate = itemView.findViewById(R.id.apointmentDate);
            avatar=itemView.findViewById(R.id.drAvatar);
        }
    }
}
