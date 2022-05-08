package com.example.tooset_test02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReviewAddActivity extends AppCompatActivity {

    RecyclerView reviewRecyclerView;
    TextView tv_reviewTitle, tv_reviewGood, tv_reviewBad;
    Button review_add_button;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;
    String userNameV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_add);

        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        tv_reviewTitle = findViewById(R.id.tv_reviewTitle);
        tv_reviewGood = findViewById(R.id.tv_reviewGood);
        tv_reviewBad = findViewById(R.id.tv_reviewBad);
        review_add_button = findViewById(R.id.review_add_button);

        mAuth = FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("User");



        review_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waringDialog();
            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();
        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    userNameV=snapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void waringDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("리뷰 작성");
        builder.setMessage("등록 후에는 수정/삭제가 불가능합니다. 등록하시겠습니까?");

        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        processInsert();
                        Intent intent = new Intent(ReviewAddActivity.this, ReviewActivity.class);
                        startActivity(intent);
                    }
                });
        builder.create().show();
    }

    private void processInsert() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", tv_reviewTitle.getText().toString());
        map.put("good_review", tv_reviewGood.getText().toString());
        map.put("bad_review", tv_reviewBad.getText().toString());
        map.put("reviewUserName", userNameV);
        map.put("now_date",getTime());


        FirebaseDatabase.getInstance().getReference().child("Reviews").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tv_reviewTitle.setText("");
                        tv_reviewGood.setText("");
                        tv_reviewBad.setText("");
                        Toast.makeText(getApplicationContext(), "리뷰 작성 완료",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "작성에 실패했습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd a KK:mm");
        String getTime = dateFormat.format(date);
        return getTime;
    }

}