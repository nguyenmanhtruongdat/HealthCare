package com.example.healthcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.databinding.ActivityCreatePostBinding;
import com.example.healthcare.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreatePostActivity extends AppCompatActivity {
    private ActivityCreatePostBinding binding;
    StorageReference postImageRef;
    private boolean shouldExit=false;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    ProgressDialog progressDialog;
    Uri imgUrl;


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Post");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        binding.imageSelect.setOnClickListener(view -> {
            selectImage();
        });


// Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

// Format the date and time components
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String date = currentDateTime.format(dateFormatter);
        String time = currentDateTime.format(timeFormatter);

// Generate the post identifier
        String postIdentifier = "post" + date.replace("/", "") + time.replace(":", "");
        binding.backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, PostHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
        binding.post.setOnClickListener(view -> {
            String title = binding.title.getText().toString();
            String content = binding.content.getText().toString();

            Post post = new Post(title, content, email, postIdentifier);
//            progressDialog = new ProgressDialog(this);
            if (binding.title.getText().toString().equals("") || binding.content.getText().toString().equals("")) {
                Toast.makeText(this, "Fill all content", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("CMM", email);
                databaseReference.child(postIdentifier).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(CreatePostActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        } else {

                            postImageRef = storage.getReference().child("post_images/" + postIdentifier + ".jpg");


                            if (imgUrl != null) {
                                postImageRef.putFile(imgUrl).addOnSuccessListener(taskSnapshot -> {
                                    binding.image.setImageURI(null);
                                    Log.d("image", "thanh cong");

                                }).addOnFailureListener(e -> {
                                    Log.d("image", "that bai roi");
//                                    if (!progressDialog.isShowing()) {
//                                        progressDialog.dismiss();
//                                    }
                                });
                            } else {
//                                if (!progressDialog.isShowing()) {
//                                    progressDialog.dismiss();
//                                }
                            }

                            Toast.makeText(CreatePostActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (shouldExit) { // shouldExit is a boolean flag to determine if the app should exit or not
            Intent intent1 = new Intent(CreatePostActivity.this, PostHomeActivity.class);
            startActivity(intent1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imgUrl = data.getData();
            binding.image.setImageURI(imgUrl);
        }
    }

}