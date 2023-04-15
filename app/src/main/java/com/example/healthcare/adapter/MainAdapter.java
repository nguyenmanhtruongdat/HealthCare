package com.example.healthcare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.healthcare.Doctors;
import com.example.healthcare.R;
import com.example.healthcare.SearchPatActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<Doctors, MainAdapter.myViewHolder> {

    private final SearchPatActivity searchPatActivity;
    private final OnItemClickListener mListener;

    public MainAdapter(@NonNull FirebaseRecyclerOptions<Doctors> options, OnItemClickListener listener, SearchPatActivity searchPatActivity) {
        super(options);
        mListener = listener;
        this.searchPatActivity= searchPatActivity;

    }


    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Doctors model) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String filePath = "profile_images/" + model.getEmail() + "_avatar.jpg"; // Example file path in Firebase Storage
        StorageReference imageRef = storageRef.child(filePath);
        myViewHolder viewHolder = (myViewHolder) holder;
// Get the download URL of the image
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Set the image to the ImageView using a library like Glide
            Glide.with(viewHolder.img.getContext())
                    .load(uri)
                    .circleCrop()
                    .into(viewHolder.img);
        }).addOnFailureListener(e -> {
            // Handle any errors
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPatActivity.onItemClick(model);
            }
        });


        // Cast the holder to your custom ViewHolder type

        // Get the data model for this position
        // Set values to views
        viewHolder.name.setText(model.getFullName());
        viewHolder.major.setText(model.getMajor());
        viewHolder.email.setText(model.getEmail());

        // Set an image to CircularImageView using a library like Glide
        Glide.with(viewHolder.img.getContext())
                .load(imageRef)
                .circleCrop()
                .into(viewHolder.img);


        viewHolder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                int itemPosition = viewHolder.getAbsoluteAdapterPosition();
                if (itemPosition != RecyclerView.NO_POSITION) {
                    Doctors doctor = getItem(itemPosition);
                    mListener.onItemClick(doctor);
                }
            }
        });


    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new myViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(Doctors doctor);
    }


    class myViewHolder extends RecyclerView.ViewHolder {
        CircularImageView img;
        MaterialTextView name, major, email;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircularImageView) itemView.findViewById(R.id.drAvatar);
            name = (MaterialTextView) itemView.findViewById(R.id.drName);
            email = (MaterialTextView) itemView.findViewById(R.id.drEmail);
            major = (MaterialTextView) itemView.findViewById(R.id.drMajor);

        }
    }

}
