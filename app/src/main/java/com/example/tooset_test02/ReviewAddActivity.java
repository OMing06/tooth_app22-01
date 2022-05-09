package com.example.tooset_test02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReviewAddActivity extends AppCompatActivity {

    EditText tv_reviewTitle, tv_reviewGood, tv_reviewBad;
    Button review_add_button, review_addImage_button;
    ImageView iv_reviewImage;
    ProgressBar progressBar2;
    RatingBar rv_ratingBar;

    Uri imageUri;
    String myUri;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef, databaseReference;
    StorageTask uploadTask;
    StorageReference storageProfileRef;

    String userNameV;
    float rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_add);

        tv_reviewTitle = findViewById(R.id.tv_reviewTitle);
        tv_reviewGood = findViewById(R.id.tv_reviewGood);
        tv_reviewBad = findViewById(R.id.tv_reviewBad);
        review_add_button = findViewById(R.id.review_add_button);
        review_addImage_button = findViewById(R.id.review_addImage_button);
        iv_reviewImage = findViewById(R.id.iv_reviewImage);
        progressBar2 = findViewById(R.id.progressBar2);
        rv_ratingBar = findViewById(R.id.rv_ratingBar);

        progressBar2.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("User");
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Reviews");

        //child()말고 getReference()에 로케이션 써주기. child()에 쓰면 갱신된다.
        //storageProfileRef = FirebaseStorage.getInstance().getReference().child("Review Pic");
        storageProfileRef = FirebaseStorage.getInstance().getReference("Review Pic");



        //이미지 추가 버튼
        review_addImage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        //업로드 버튼
        review_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waringDialog();
            }
        });


    }

    @Override
    protected void onStart() { //유저 이름 찾아오기
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

                        final String t1 = tv_reviewTitle.getText().toString();
                        final String t2 = tv_reviewGood.getText().toString();
                        final String t3 = tv_reviewBad.getText().toString();
                        final float rating = rv_ratingBar.getRating();
                        if(!(t1.isEmpty() && t2.isEmpty() || t3.isEmpty()) && imageUri != null) {
                        processInsert();
                        Intent intent = new Intent(ReviewAddActivity.this, ReviewActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ReviewAddActivity.this, "내용을 채워주세요.", Toast.LENGTH_LONG).show();

                        }
                    }
                });
        builder.create().show();
    }

    //선택한 사진 가져오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            imageUri = data.getData();
            iv_reviewImage.setImageURI(imageUri);
            //uploadPicture();
        } else { }
    }

    private String getFileExtension(Uri mUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(mUri));
    }

    private void processInsert() {
        final StorageReference fileRef = storageProfileRef
                .child(mAuth.getCurrentUser().getUid() + ".jpg");
        uploadTask = fileRef.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if(!task.isSuccessful()) {
                    throw task.getException();
                }

                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()) {


                    Map<String, Object> map = new HashMap<>();
                    map.put("title", tv_reviewTitle.getText().toString());
                    map.put("good_review", tv_reviewGood.getText().toString());
                    map.put("bad_review", tv_reviewBad.getText().toString());
                    map.put("reviewUserName", userNameV);
                    map.put("now_date",getTime());
                    map.put("rating", rv_ratingBar.getRating());
                    Uri downloadUrl = task.getResult();
                    myUri = downloadUrl.toString();
                    map.put("imageUrl", myUri);

                    FirebaseDatabase.getInstance().getReference().child("Reviews").push()
                            .setValue(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    tv_reviewTitle.setText("");
                                    tv_reviewGood.setText("");
                                    tv_reviewBad.setText("");
                                    rv_ratingBar.setRating(rating);
                                    //iv_reviewImage.setImageResource(myUri);
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

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*"); //이미지만
        // intent.setType("image/* video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
}