package com.example.tooset_test02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewDetail extends AppCompatActivity {

    TextView tv_rvName, tv_rvDate, tv_rvTitle, tv_rvGood, tv_rvBad, tv_rvEmail;
    ImageView iv_rvImage;

    String myUri;

    FirebaseDatabase mDatabase;
    DatabaseReference mDbRef;

    ReviewModel reviewModel;
    String title, good_review, bad_review, now_date, imageUrl, reviewUserName, reviewUserEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        tv_rvName = findViewById(R.id.tv_rvName);
        tv_rvEmail = findViewById(R.id.tv_rvEmail);
        tv_rvDate = findViewById(R.id.tv_rvDate);
        tv_rvTitle = findViewById(R.id.tv_rvTitle);
        tv_rvGood = findViewById(R.id.tv_rvGood);
        tv_rvBad = findViewById(R.id.tv_rvBad);
        iv_rvImage = findViewById(R.id.iv_rvImage);

        mDatabase = FirebaseDatabase.getInstance();
        mDbRef = mDatabase.getReference().child("Reviews");

        reviewDetail();

    }

    private void reviewDetail() {
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    reviewModel = ds.getValue(ReviewModel.class);

                    reviewUserName = getIntent().getStringExtra("reviewUserName");
                    reviewUserEmail = getIntent().getStringExtra("reviewUserEmail");
                    now_date = getIntent().getStringExtra("now_date");
                    title = getIntent().getStringExtra("title");
                    good_review = getIntent().getStringExtra("good_review");
                    bad_review = getIntent().getStringExtra("bad_review");
                    imageUrl = getIntent().getStringExtra("imageUrl");

                    tv_rvName.setText(reviewUserName);
                    tv_rvEmail.setText(reviewUserEmail);
                    tv_rvDate.setText(now_date);
                    tv_rvTitle.setText(title);
                    tv_rvGood.setText(good_review);
                    tv_rvBad.setText(bad_review);
                    Glide.with(ReviewDetail.this).load(imageUrl).error(R.drawable.no_picture_image).into(iv_rvImage);


                    myUri = null;
                    myUri = reviewModel.getImageUrl();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}