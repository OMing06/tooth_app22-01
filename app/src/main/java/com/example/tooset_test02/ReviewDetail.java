package com.example.tooset_test02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    Button favoriteBtn;

    Uri imageUri;
    String myUri;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef, databaseReference;
    StorageTask uploadTask;
    StorageReference storageProfileRef;

    private DatabaseReference mDatabase;


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

        //reviewDetail();

    }

    /*private void reviewDetail() {
        databaseReference.child("Reviews").child(Gname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //각각의 값 받아오기 get어쩌구 함수들은 Together_group_list.class에서 지정한것
                gintro = group.getGintro();
                goaltime = group.getGoaltime();
                gdate = group.getGoalday();

                //텍스트뷰에 받아온 문자열 대입하기
                goaltime_tv.setText(goaltime);
                gintro_tv.setText(gintro);
                gdate_tv.setText(gdate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        })

    }*/
}