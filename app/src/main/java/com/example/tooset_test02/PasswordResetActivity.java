package com.example.tooset_test02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    EditText write_resetEmail;
    Button sendEmail_button;

    private FirebaseAuth mAuth;
    private static final String TAG = "PasswordResetActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        write_resetEmail = findViewById(R.id.write_resetEmail);
        sendEmail_button = findViewById(R.id.sendEmail_button);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance(); //유저 정보 인스턴스 생성

        sendEmail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetPassword();
            }
        });

    }


    private void sendResetPassword() {
        // 이메일
        String email = write_resetEmail.getText().toString();

        //String emailAddress = "user@example.com";

        if (email.length() > 0) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "보냄");
                                toastMessage("보내기에 성공했습니다.");
                            }
                        }
                    });
        } else {
            toastMessage("이메일을 입력해주세요.");
        }
    }

    private void toastMessage(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }
}