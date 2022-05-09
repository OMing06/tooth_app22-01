package com.example.tooset_test02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class JoinActivity extends AppCompatActivity {

    private static final String TAG = "JoinActivity";
    private FirebaseAuth mAuth;

    EditText write_email, write_password, write_passCk;
    Button join_button, login_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        write_email = findViewById(R.id.login_email);
        write_password = findViewById(R.id.login_password);
        write_passCk = findViewById(R.id.write_passCk);
        join_button = findViewById(R.id.join_button);
        login_intent = findViewById(R.id.login_intent);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        login_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // When initializing your Activity, check to see if the user is currently signed in.
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void signUp() {
        // 이메일
        String email = write_email.getText().toString();
        // 비밀번호
        String password = write_password.getText().toString();
        //비밀번호 체크
        String passwordCk = write_passCk.getText().toString();

        if (email.length() > 0 && password.length() > 0 && passwordCk.length() > 5) {
            if (password.equals(passwordCk)) {

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        //Intent intent = new Intent(JoinActivity.this, UserAddActivity.class);
                                        //startActivity(intent);
                                        intentFlagActivity(EmailVerifyActivity.class);
                                        Toast.makeText(JoinActivity.this, "회원가입 성공. 이메일 인증을 진행해주세요. 메일함을 확인해주세요.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } //if(task.isSuccessful())
                                    else {
                                        Toast.makeText(JoinActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                } //public void onComplete(@NonNull Task<Void> task)
                            });

                        } else {
                            Toast.makeText(JoinActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                });



                /*mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {

                                                Intent intent = new Intent(JoinActivity.this, UserAddActivity.class);
                                                startActivity(intent);
                                                Toast.makeText(JoinActivity.this, "등록 완료", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                if (task.getException() != null) {
                                                    // If sign in fails, display a message to the user.
                                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                    Toast.makeText(JoinActivity.this, "회원가입 실패.",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                    })



                                }
                            }
                        });*/
                /////////////////


            }
        } else if (passwordCk.length() <= 6) {
            Toast.makeText(JoinActivity.this, "비밀번호를 6자리 이상 입력해주세요", Toast.LENGTH_LONG).show();
        } else if (!password.equals(passwordCk)) {
            Toast.makeText(JoinActivity.this, "회원가입 실패. 아이디 또는 비밀번호를 확인해보세요.",
                    Toast.LENGTH_LONG).show();
        }
    }

    Intent intent = getIntent();

    private void intentFlagActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}