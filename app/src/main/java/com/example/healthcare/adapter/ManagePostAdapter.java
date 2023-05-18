package com.example.healthcare.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.EditPostActivity;
import com.example.healthcare.ManagePostActivity;
import com.example.healthcare.R;
import com.example.healthcare.model.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ManagePostAdapter extends FirebaseRecyclerAdapter<Post, ManagePostAdapter.myViewHolder> {

    private final ManagePostActivity activity;
    private OnItemClickListener mListener;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference mDatabaseReference;
    private String email;

    public ManagePostAdapter(@NonNull FirebaseRecyclerOptions<Post> options, ManagePostActivity activity, OnItemClickListener mListener) {
        super(options);
        this.activity = activity;
        this.mListener = mListener;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_s, parent, false);
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
//        viewHolder.author.setText(model.getAuthor());
        viewHolder.content.setText(model.getContent());

        final long ONE_MEGABYTE = 1024 * 1024;
        StorageReference profileImageRef = storage.getReference().child("post_images/" + model.getId() + ".jpg");
        profileImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            // Create a Bitmap object from the byte array
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // Use the bitmap object to display the image in your app
            viewHolder.img.setImageBitmap(bitmap);

        }).addOnFailureListener(exception -> {
            // Handle any errors that occur during the download
            viewHolder.img.setImageResource(R.drawable.defaul_avatar);

        });

        viewHolder.edit.setOnClickListener(view -> {
            Post post = new Post(model.getTitle(), model.getContent(), model.getAuthor(), model.getId());
            Intent intent = new Intent(view.getContext(), EditPostActivity.class);
            // Pass the necessary data to the EditPostActivity
            intent.putExtra("post", post);
            view.getContext().startActivity(intent);
        });


        viewHolder.del.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomDialog);
            builder.setTitle("Confirm delete");
            builder.setMessage("Do you want to delete this post?");

            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Post").child(model.getId());
                postRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Post successfully deleted
                        Toast.makeText(activity, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Toast.makeText(activity, "Post deleted failed", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
            int buttonYesColor = ContextCompat.getColor(activity, R.color.appColor);
            int buttonNoColor = ContextCompat.getColor(activity, R.color.appColor);

// Set the button text color

            Button yesButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button noButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            yesButton.setTextColor(buttonYesColor);
            noButton.setTextColor(buttonYesColor);

        });

//        StorageReference drAvatarProfile = storage.getReference().child("profile_images/" + model.getAuthor() + "_avatar.jpg");
//        drAvatarProfile.getBytes(ONE_MEGABYTE)
//                .addOnSuccessListener(bytes -> {
//                    // Create a Bitmap object from the byte array
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    // Use the bitmap object to display the image in your app
//                    viewHolder.authorAvt.setImageBitmap(bitmap);
//
//                })
//                .addOnFailureListener(exception -> {
//                    // Handle any errors that occur during the download
//                    viewHolder.authorAvt.setImageResource(R.drawable.defaul_avatar);
//                });

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
        MaterialTextView title, content;

        ImageView img;
        MaterialButton edit, del;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            img = itemView.findViewById(R.id.image);
            edit = itemView.findViewById(R.id.edit);
            del = itemView.findViewById(R.id.delete);

        }
    }
}
