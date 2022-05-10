package com.example.tooset_test02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class ReviewDetail extends AppCompatActivity {

    TextView tv_rvName, tv_rvDate, tv_rvTitle, tv_rvGood, tv_rvBad;
    ImageView iv_rvImage;

    Uri imageUri;
    String myUri;

    FirebaseDatabase mDatabase;
    DatabaseReference mDbRef;

    ReviewModel reviewModel;

    String title, good_review, bad_review, now_date, imageUrl, reviewUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        tv_rvName = findViewById(R.id.tv_rvName);
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

                    reviewUserName = reviewModel.getReviewUserName();
                    now_date = reviewModel.getNow_date();
                    title = reviewModel.getTitle();
                    good_review = reviewModel.getGood_review();
                    bad_review = reviewModel.getBad_review();
                    imageUrl = reviewModel.getImageUrl();

                    tv_rvName.setText(reviewUserName);
                    tv_rvDate.setText(now_date);
                    tv_rvTitle.setText(title);
                    tv_rvGood.setText(good_review);
                    tv_rvBad.setText(bad_review);
                    Glide.with(ReviewDetail.this).load(imageUrl).error(R.drawable.no_picture_image).into(iv_rvImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}