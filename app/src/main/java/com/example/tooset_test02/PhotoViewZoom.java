package com.example.tooset_test02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PhotoViewZoom extends AppCompatActivity {

    ImageView imageView5;
    PhotoView photoView;

    FirebaseDatabase mDatabase;
    DatabaseReference mDbRef;
    StorageReference storageProfileRef;

    ReviewModel reviewModel;

    String imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        //imageView5 = findViewById(R.id.imageView5);
        photoView = findViewById(R.id.photo_view);

        mDatabase = FirebaseDatabase.getInstance();
        mDbRef = mDatabase.getReference().child("Reviews");
        storageProfileRef = FirebaseStorage.getInstance().getReference("Review Pic");

        reviewDetail();

    }

    private void reviewDetail() {
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    reviewModel = ds.getValue(ReviewModel.class);

                    imageUrl = getIntent().getStringExtra("imageUrl");
                    Glide.with(PhotoViewZoom.this).load(imageUrl).error(R.drawable.no_picture_image).into(photoView);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}