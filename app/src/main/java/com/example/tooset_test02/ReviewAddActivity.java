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

    //private static final int PICK_IMAGE_REQUEST = 1;

    TextView tv_reviewTitle, tv_reviewGood, tv_reviewBad;
    Button review_add_button, review_addImage_button;
    ImageView iv_reviewImage;
    ProgressBar progressBar2;

    Uri imageUri;
    String myUri = "";

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef, databaseReference;
    StorageTask uploadTask;
    StorageReference storageProfileRef;

    String userNameV;
    ReviewModel reviewModel;

    //getImageUrl

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

        progressBar2.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Reviews");
        storageProfileRef = FirebaseStorage.getInstance().getReference().child("Review Pic");


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
        //map.put("imageUrl", databaseReference.push().getKey());
        map.put("now_date",getTime());





        final StorageReference fileRef = storageProfileRef.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String t= task.getResult().toString();

                        DatabaseReference newPost=databaseReference.push();
                        map.put("imageUrl", newPost.child("imageUrl").setValue(task.getResult().toString()));
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar2.setVisibility(View.VISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar2.setVisibility(View.INVISIBLE);
                Toast.makeText(ReviewAddActivity.this, "업로드실패애애애ㅐ", Toast.LENGTH_LONG).show();
            }
        });









        FirebaseDatabase.getInstance().getReference().child("Reviews").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tv_reviewTitle.setText("");
                        tv_reviewGood.setText("");
                        tv_reviewBad.setText("");
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

/*    private void uploadReviewImage(Uri uri) {

        final StorageReference fileRef = storageProfileRef.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        ReviewModel reviewModel = new ReviewModel();
                        String reviewModelId = databaseReference.push().getKey();
                        databaseReference.child(reviewModelId).setValue(reviewModel);


                        progressBar2.setVisibility(View.INVISIBLE);
                        Toast.makeText(ReviewAddActivity.this, "tjdrhd", Toast.LENGTH_LONG).show();
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar2.setVisibility(View.VISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar2.setVisibility(View.INVISIBLE);
                Toast.makeText(ReviewAddActivity.this, "업로드실패애애애ㅐ", Toast.LENGTH_LONG).show();
            }
        });

    }*/

    private String getFileExtension(Uri mUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(mUri));
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