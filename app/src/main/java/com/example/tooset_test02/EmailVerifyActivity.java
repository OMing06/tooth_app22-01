package com.example.tooset_test02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmailVerifyActivity extends AppCompatActivity {

    TextView tv_emailVerify;
    Button button_logout;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        tv_emailVerify =findViewById(R.id.tv_emailVerify);
        button_logout = findViewById(R.id.button_logout);

        mAuth = FirebaseAuth.getInstance(); //인스턴스 생성
        user = FirebaseAuth.getInstance().getCurrentUser(); //인스턴스 생성 후 사용자 정보 가져오기
        db = FirebaseFirestore.getInstance();

        getUserInfo();

        button_logout.setOnClickListener(new View.OnClickListener() { //로그아웃
            @Override
            public void onClick(View v) {
                logOut();
            }
        });


    }

    private void logOut() {
        mAuth.signOut();
        intentActivity(LoginActivity.class);
        finish();
    }

    private void getUserInfo() { //이메일정보 가져와 텍스트뷰에 세팅
        String email = tv_emailVerify.getText().toString();
        email = mAuth.getCurrentUser().getEmail();
        tv_emailVerify.setText(email);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tooset_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mAuth.signOut();
        intentActivity(LoginActivity.class);
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void intentActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}