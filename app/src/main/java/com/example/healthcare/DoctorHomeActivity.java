package com.example.healthcare;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.healthcare.databinding.ActivityDoctorHomeBinding;
import com.example.healthcare.fragment.DoctorProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

public class DoctorHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private boolean shouldExit = false;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private ActivityDoctorHomeBinding binding;
    private DatabaseReference mDatabase;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        System.out.println(token);
                        // Log and toast
                        Log.d("FCM", token);
                    }
                });

        binding.navView.setNavigationItemSelectedListener(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Doctors");
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        DatabaseReference userRef = mDatabase.child(uid);
        StorageReference profileImageRef = storage.getReference().child("profile_images/" + user.getEmail() + "_avatar.jpg");
        View headerView = binding.navView.getHeaderView(0);
        Menu menu = binding.navView.getMenu();
        MenuItem logout = menu.findItem(R.id.nav_logout);
        MenuItem profile = menu.findItem(R.id.nav_profile);
        NavigationView navigationView = findViewById(R.id.nav_view);


        CircularImageView nav_avtar = headerView.findViewById(R.id.nav_avatar);
        TextView nav_name = headerView.findViewById(R.id.nav_name);
        TextView nav_email = headerView.findViewById(R.id.nav_email);
        MenuItem nav_logout = headerView.findViewById(R.id.nav_logout);
        final long ONE_MEGABYTE = 1024 * 1024;
        profileImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes -> {
                    // Create a Bitmap object from the byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    // Use the bitmap object to display the image in your app
                    binding.avtProfile.setImageBitmap(bitmap);
                    nav_avtar.setImageBitmap(bitmap);

                })
                .addOnFailureListener(exception -> {
                    // Handle any errors that occur during the download
                    binding.avtProfile.setImageResource(R.drawable.defaul_avatar);
                    nav_avtar.setImageResource(R.drawable.defaul_avatar);
                });

        binding.avtProfile.setOnClickListener(view1 -> {
            binding.drawerLayout.openDrawer(GravityCompat.END);
        });
        binding.post.setOnClickListener(view1 -> {
            Intent intent = new Intent(this,  PostHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        });
        binding.appointmentRequest.setOnClickListener(view1 -> {
            Intent intent = new Intent(this, AppointmentRequestActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        binding.myPatient.setOnClickListener(view1 -> {
            Intent intent = new Intent(this, MyPatientActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });


        if (user != null) {
            FirebaseDatabase.getInstance().getReference().child("Doctors").child(user.getEmail().split("@")[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String fullName = snapshot.child("fullName").getValue(String.class);
                        String role = snapshot.child("role").getValue(String.class);
                        String email = user.getEmail();
                        binding.fullName.setText(fullName);
                        binding.email.setText(email + " | " + role);
                        nav_name.setText(fullName);
                        nav_email.setText(email);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(DoctorHomeActivity.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseDatabase.getInstance().getReference().child("Doctors").child(user.getEmail().split("@")[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(DoctorHomeActivity.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent homeIntent = new Intent(this, DoctorHomeActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                return true;
            case R.id.nav_profile:
                binding.mainLayout.removeAllViews();

//                Intent intent = new Intent(this, UserProfileActivity.class);
//                startActivity(intent);
//                finish();
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DoctorProfileFragment()).commit();
                break;
            case R.id.nav_share:
//                binding.mainLayout.removeAllViews();
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
// Example: content://com.google.android.apps.photos.contentprovider/...
                shareIntent.putExtra(Intent.EXTRA_STREAM, "Check out this Application");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Your application link");
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share via"));
                //Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
                break;
            case R.id.nav_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorHomeActivity.this, R.style.CustomDialog);
                builder.setMessage("Are you sure you want to log out?");
                builder.setCancelable(false);
                builder.setNegativeButton("No", (dialog, id) -> {
                    // If the user cancels, close the dialog
                    dialog.cancel();
                });
                builder.setPositiveButton("Yes", (dialog, id) -> {
                    // If the user confirms, sign them out and redirect to login activity
                    mAuth.signOut();
                    Intent intent1 = new Intent(DoctorHomeActivity.this, LoginActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Log out");
                alertDialog.show();
                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.dialog_button_text_color));
                Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(getResources().getColor(R.color.dialog_button_text_color));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        binding.drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (shouldExit) { // shouldExit is a boolean flag to determine if the app should exit or not
            super.onBackPressed();


        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    public void navigateToHome() {
        Intent intent = new Intent(this, DoctorHomeActivity.class);
        startActivity(intent);
        finish();
    }

}
