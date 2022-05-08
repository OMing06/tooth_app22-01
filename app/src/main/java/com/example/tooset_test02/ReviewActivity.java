package com.example.tooset_test02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    FloatingActionButton add_reviewButton;
    RecyclerView recyclerView;

    ReviewAdapter reviewAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db;
    FirebaseUser user;
    private static final String TAG = "ReviewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        setTitle("한 줄 후기");

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        add_reviewButton = findViewById(R.id.add_reviewButton);

        recyclerView = findViewById(R.id.reviewRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);


        FirebaseRecyclerOptions<ReviewModel> options =
                new FirebaseRecyclerOptions.Builder<ReviewModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Reviews"), ReviewModel.class)
                        .build();


        add_reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewActivity.this, ReviewAddActivity.class);
                startActivity(intent);
            }
        });




        reviewAdapter = new ReviewAdapter(options);
        recyclerView.setAdapter(reviewAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        reviewAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        reviewAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.review_search_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                textSearch(query);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void textSearch(String str) {
        FirebaseRecyclerOptions<ReviewModel> options =
                new FirebaseRecyclerOptions.Builder<ReviewModel>()
                        .setQuery(FirebaseDatabase
                                .getInstance().getReference()
                                .child("Reviews")
                                .orderByChild("title")//검색기준
                                .startAt(str).endAt(str+"~"), ReviewModel.class)
                        .build();

        reviewAdapter = new ReviewAdapter(options);
        reviewAdapter.startListening();
        recyclerView.setAdapter(reviewAdapter);

    }
}