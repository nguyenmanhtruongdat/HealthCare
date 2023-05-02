package com.example.healthcare;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import com.example.healthcare.databinding.ActivityHomePatientAcitivityBinding;
import com.example.healthcare.fragment.AboutFragment;
import com.example.healthcare.fragment.ProfileFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

public class HomePatientActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private boolean shouldExit = false;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private ActivityHomePatientAcitivityBinding binding;
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    GoogleSignInOptions gso;
    GoogleSignInAccount account;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePatientAcitivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);
        binding.navView.setNavigationItemSelectedListener(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users");
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        DatabaseReference userRef = mDatabase.child(uid);
        StorageReference profileImageRef = storage.getReference().child("profile_images/" + uid + "_avatar.jpg");
        View headerView = binding.navView.getHeaderView(0);
        Menu menu = binding.navView.getMenu();
        MenuItem logout = menu.findItem(R.id.nav_logout);
        MenuItem profile = menu.findItem(R.id.nav_profile);
        NavigationView navigationView = findViewById(R.id.nav_view);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String personFullName = account.getDisplayName();
            String personEmail = account.getEmail();
            binding.email.setText(personEmail);
            binding.fullName.setText(personFullName);
        }
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


        if (user != null) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    Toast.makeText(HomePatientActivity.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        binding.search.setOnClickListener(view3 -> {
            Intent intent = new Intent(HomePatientActivity.this, SearchPatActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.appointmentRequired.setOnClickListener(view1 -> {
            Intent intent = new Intent(HomePatientActivity.this, AppointmentRequiredActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        binding.appointmentToday.setOnClickListener(view1 -> {
            Intent intent = new Intent(HomePatientActivity.this, AppointmentTodayActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        binding.bmiCal.setOnClickListener(view1 -> {
            Intent intent = new Intent(HomePatientActivity.this, BMICalculator.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });

//        binding.myHealth.setOnClickListener(view1 -> {
//            Intent intent = new Intent(HomePatientActivity.this, MyHealthActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//        });

//        binding.bookingDoctor.setOnClickListener(view4 ->{
//            Intent intent = new Intent(HomePatientActivity.this, BookingDoctorActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String fullName = snapshot.child("fullName").getValue(String.class);
                        //binding.welcomeText.setText("Welcome " + fullName + "!");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(HomePatientActivity.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent homeIntent = new Intent(this, HomePatientActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                return true;
            case R.id.nav_profile:
                binding.mainLayout.removeAllViews();

//                Intent intent = new Intent(this, UserProfileActivity.class);
//                startActivity(intent);
//                finish();
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
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
            case R.id.nav_about:
                binding.mainLayout.removeAllViews();

                //Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();
//                fragment_container.removeAllViews();
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
                break;
            case R.id.nav_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(HomePatientActivity.this, R.style.CustomDialog);
                builder.setMessage("Are you sure you want to log out?");
                builder.setCancelable(false);
                builder.setNegativeButton("No", (dialog, id) -> {
                    // If the user cancels, close the dialog
                    dialog.cancel();
                });
                builder.setPositiveButton("Yes", (dialog, id) -> {
                    // If the user confirms, sign them out and redirect to login activity
                    mAuth.signOut();

                    if (account != null) {
                        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                                startActivity(new Intent(HomePatientActivity.this, LoginActivity.class));
                            }
                        });
                    }

                    Intent intent1 = new Intent(HomePatientActivity.this, LoginActivity.class);
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
        Intent intent = new Intent(this, HomePatientActivity.class);
        startActivity(intent);
        finish();
    }

}
