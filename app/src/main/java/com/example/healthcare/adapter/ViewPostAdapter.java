package com.example.healthcare.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.NewsPostActivity;
import com.example.healthcare.R;
import com.example.healthcare.model.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ViewPostAdapter extends FirebaseRecyclerAdapter<Post, ViewPostAdapter.myViewHolder> {

    private final NewsPostActivity activity;
    private OnItemClickListener mListener;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference mDatabaseReference;
    private String email;

    public ViewPostAdapter(@NonNull FirebaseRecyclerOptions<Post> options, NewsPostActivity activity, OnItemClickListener mListener) {
        super(options);
        this.activity = activity;
        this.mListener = mListener;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return getSnapshots().size();
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Post model) {

        myViewHolder viewHolder = (myViewHolder) holder;
// Get the download URL of the image

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                activity.onItemClick(model);
//            }
//        });

        // Cast the holder to your custom ViewHolder type

        // Get the data model for this position
        // Set values to views
        viewHolder.title.setText(model.getTitle());
        viewHolder.author.setText(model.getAuthor());
        viewHolder.content.setText(model.getContent());

        final long ONE_MEGABYTE = 1024 * 1024;
        StorageReference profileImageRef = storage.getReference().child("post_images/" + model.getId() + ".jpg");
        profileImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes -> {
                    // Create a Bitmap object from the byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    // Use the bitmap object to display the image in your app
                    viewHolder.img.setImageBitmap(bitmap);

                })
                .addOnFailureListener(exception -> {
                    // Handle any errors that occur during the download
                    viewHolder.img.setImageResource(R.drawable.defaul_avatar);
                });

        StorageReference drAvatarProfile = storage.getReference().child("profile_images/" + model.getAuthor() + "_avatar.jpg");
        drAvatarProfile.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes -> {
                    // Create a Bitmap object from the byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    // Use the bitmap object to display the image in your app
                    viewHolder.authorAvt.setImageBitmap(bitmap);

                })
                .addOnFailureListener(exception -> {
                    // Handle any errors that occur during the download
                    viewHolder.authorAvt.setImageResource(R.drawable.defaul_avatar);
                });

//        viewHolder.email.setText(model.getUserPhone());


        // Set an image to CircularImageView using a library like Glide


        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                int itemPosition = holder.getAbsoluteAdapterPosition();
                if (itemPosition != RecyclerView.NO_POSITION) {
                    Post request = getItem(itemPosition);
                    mListener.onItemClick(request);
                }
            }
        });
    }


    public interface OnItemClickListener {
        void onItemClick(Post request);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView title, content, author;
        CircularImageView authorAvt;

        ImageView img;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            authorAvt = itemView.findViewById(R.id.authorAvt);
            content = itemView.findViewById(R.id.content);
            img = itemView.findViewById(R.id.image);
        }
    }
}
