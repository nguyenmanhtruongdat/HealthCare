package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.healthcare.adapter.MainAdapter;
import com.example.healthcare.databinding.ActivitySearchPatBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchPatActivity extends AppCompatActivity implements MainAdapter.OnItemClickListener {
    MainAdapter mainAdapter;
    private ActivitySearchPatBinding binding;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchPatBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());
        binding.resultList.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Doctors> options =
                new FirebaseRecyclerOptions.Builder<Doctors>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Doctors"), Doctors.class)
                        .build();
        mainAdapter = new MainAdapter(options, this, SearchPatActivity.this);
        binding.resultList.setAdapter(mainAdapter);

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                Log.d("SearchPatActivity: ", query);
//                savedInstanceState.putString("SEARCH_VIEW_TEXT", binding.search.getQuery().toString());
//                datgsk5@gmail.com
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.d("SearchPatActivity: ", query);
//                savedInstanceState.putString("SEARCH_VIEW_TEXT", binding.search.getQuery().toString());
                txtSearch(query);
                return false;
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("SEARCH_VIEW_TEXT", binding.search.getQuery().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String searchViewText = savedInstanceState.getString("SEARCH_VIEW_TEXT");
        binding.search.setQuery(searchViewText, false);
    }

    public void txtSearch(String string) {
        FirebaseRecyclerOptions<Doctors> options =
                new FirebaseRecyclerOptions.Builder<Doctors>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Doctors").orderByChild("fullName").startAt(string).endAt(string + "\uf8ff"), Doctors.class)
                        .build();
        mainAdapter = new MainAdapter(options, this, SearchPatActivity.this);
        mainAdapter.startListening();
        if (binding.resultList != null) {
            binding.resultList.setAdapter(mainAdapter);
        } else {
            Log.d("SearchPatActivity", "recyclerView is null");
        }
        FirebaseDatabase.getInstance().getReference().child("Doctors")
                .orderByChild("fullName").startAt(string).endAt(string + "~")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("SearchPatActivity", "query result count: " + snapshot.getChildrenCount());
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Log.d("SearchPatActivity", "query result: " + child.getValue());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("SearchPatActivity", "query cancelled", error.toException());
                    }
                });

    }


    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();

    }

    @Override
    public void onItemClick(Doctors doctor) {
        Intent intent = new Intent(this, DoctorInformationBook.class);
        intent.putExtra("doctor", doctor);
        Log.d("Bac si: ", doctor.getFullName() + " va " + doctor.getEmail());
        startActivity(intent);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}