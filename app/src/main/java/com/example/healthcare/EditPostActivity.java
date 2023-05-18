package com.example.healthcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityEditPostBinding;
import com.example.healthcare.model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditPostActivity extends AppCompatActivity {
    private ActivityEditPostBinding binding;
    StorageReference profileImageRef;
    ProgressDialog progressDialog;
    Post post;
    Uri imgUrl;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityEditPostBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ManagePostActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
        Intent intent = getIntent();
        if (intent != null) {
            post = (Post) intent.getSerializableExtra("post");

            binding.title.setText(post.getTitle());
            binding.content.setText(post.getContent());
        }
        binding.image.setOnClickListener(view -> {
            selectImage();
        });
        profileImageRef = storage.getReference().child("post_images/" + post.getId() + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        profileImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes -> {
                    // Create a Bitmap object from the byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    // Use the bitmap object to display the image in your app
                    binding.image.setImageBitmap(bitmap);
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        binding.image.setImageResource(R.drawable.defaul_avatar);
                    }
                });

        binding.post.setOnClickListener(view1 -> {
//            progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
            String title = binding.title.getText().toString().trim();
            String content = binding.content.getText().toString().trim();


            String userId = FirebaseAuth.getInstance().getUid();
            profileImageRef = storage.getReference().child("post_images/" + post.getId() + ".jpg");

            if (imgUrl != null) {
                profileImageRef.putFile(imgUrl).addOnSuccessListener(taskSnapshot -> {
                    binding.image.setImageURI(null);
                    Log.d("image", "thanh cong");
//                    if (!progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                    }
                }).addOnFailureListener(e -> {
                    Log.d("image", "that bai roi");
//                    if (!progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                    }
                });
            } else {
//                if (!progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
            }

            Post post1 = new Post(title, content, post.getAuthor(), post.getId());
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Post").child(post.getId());
            databaseReference.setValue(post1).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(EditPostActivity.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditPostActivity.this, "Failed to update post", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imgUrl = data.getData();
            binding.image.setImageURI(imgUrl);
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

}