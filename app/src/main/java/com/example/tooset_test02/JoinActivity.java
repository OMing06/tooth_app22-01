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
        mAuth = FirebaseAuth.getInstance(); //유저정보 인스턴스 생성


        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        login_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentActivity(LoginActivity.class);
            }
        });
    }

    // When initializing your Activity, check to see if the user is currently signed in.
    @Override
    public void onStart() { //로그인 여부 확인
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

        if (email.length() > 0 && password.length() > 5 && passwordCk.length() > 5) {
            if (password.equals(passwordCk)) {

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        intentFlagActivity(UserAddActivity.class); //회원정보 입력(닉네임 등..)
                                        toastMessage("이메일 인증을 위해 메일함을 확인해주세요.");
                                        //finish();
                                    } //if(task.isSuccessful()) 끝
                                    else {
                                        toastMessage(task.getException().getMessage());
                                    }
                                } //public void onComplete(@NonNull Task<Void> task) 끝
                            });

                        } else {
                            toastMessage(task.getException().getMessage());
                        }
                    }
                });
            } else if (!password.equals(passwordCk)) {
                toastMessage("아이디 또는 비밀번호를 확인해주세요.");
            }
        } else if (password.length() <= 6) { //비밀번호는 6자리 이상 입력해야 한다.
            toastMessage("비밀번호를 6자리 이상 입력해주세요.");
        }
    }

    Intent intent = getIntent();

    private void intentFlagActivity(Class c) { //뒤로가기를 누르면 어플이 바로 종료되게끔.
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void intentActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    private void toastMessage(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }
}