package com.example.tooset_test02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tooset_test02.MainActivity;
import com.example.tooset_test02.R;
import com.example.tooset_test02.TemHumActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAddActivity extends AppCompatActivity {

    private static final String TAG = "UserAddActivity";

    EditText write_nickname;
    TextView tv_profileEmail;
    Button profile_add_button, revokeUser_button;
    CircleImageView profile_image;
    FloatingActionButton imagePick_button;
    ProgressBar progressBar;

    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageProfileRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);


        write_nickname = findViewById(R.id.write_nickname);
        profile_add_button = findViewById(R.id.profile_add_button);
        profile_image = findViewById(R.id.profile_image);
        imagePick_button = findViewById(R.id.imagePick_button);
        progressBar = findViewById(R.id.progressBar);
        revokeUser_button = findViewById(R.id.revokeUser_button);
        tv_profileEmail = findViewById(R.id.tv_profileEmail);

        progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        storageProfileRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");
        user = mAuth.getInstance().getCurrentUser();

        imagePick_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        profile_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndsave();
                //uploadProfileImage();
            }
        });

        revokeUser_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reVokeUser_dialog1();
            }
        });

        getUserInfo();
    }

    private void validateAndsave() {
        if(TextUtils.isEmpty(write_nickname.getText().toString())) {
            Toast.makeText(this, "????????? ??????????????????.", Toast.LENGTH_LONG).show();
        } else {
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("name", write_nickname.getText().toString());

            databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);
            uploadProfileImage();
        }
    }

    private void getUserInfo() {
        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String email = tv_profileEmail.getText().toString();
                email = mAuth.getCurrentUser().getEmail();
                tv_profileEmail.setText(email);

                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    String name = dataSnapshot.child("name").getValue().toString();

                    write_nickname.setText(name);

                    if(dataSnapshot.hasChild("image")) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).error(R.drawable.no_picture_image).into(profile_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            imageUri = data.getData();
            profile_image.setImageURI(imageUri);
            //uploadPicture();
        } else {
            //Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProfileImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set your profile");
        progressDialog.setMessage("???????????? ?????? ????????????...");
        progressDialog.show();

        //if(imageUri != null) {
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
                    Uri downloadUrl = task.getResult();
                    myUri = downloadUrl.toString();

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("image", myUri);

                    databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);

                    progressDialog.dismiss();
                    intentFlagActivity(MainActivity.class);
                    //finish();
                }
            }
        });
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        // intent.setType("image/* video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private void revokeUser() {
        mAuth.getCurrentUser().delete();
    }


    void reVokeUser_dialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("?????? ?????????????????????????");
        builder.setMessage("?????? ??? ???????????? ????????????.");
        builder.setPositiveButton("???", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reVokeUser_dialog2();
            }
        });
        builder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        builder.create().show();
    }


    void reVokeUser_dialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("?????? ??? ??? ??????????????????.");
        builder.setMessage("?????? ??? ???????????? ????????????.");
        builder.setPositiveButton("???", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                revokeUser();
                Toast.makeText(UserAddActivity.this, "bye", Toast.LENGTH_SHORT).show();
                intentFlagActivity(LoginActivity.class);
            }
        });
        builder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        builder.create().show();
    }

    private void intentFlagActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}