package com.example.tooset_test02;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "JoinActivity";
    private FirebaseAuth mAuth;

    EditText login_email, login_password;
    Button login_button, join_intent, pwReset_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_button = findViewById(R.id.login_button);
        join_intent = findViewById(R.id.join_intent);
        pwReset_button = findViewById(R.id.pwReset_button);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        join_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentActivity(JoinActivity.class);
            }
        });


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUp();
            }
        });

        pwReset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentActivity(PasswordResetActivity.class);
            }
        });
    }

    // When initializing your Activity, check to see if the user is currently signed in.
    @Override
    public void onStart() { //????????? ?????? ??????
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void LoginUp() {
        // ?????????
        String email = login_email.getText().toString();
        // ????????????
        String password = login_password.getText().toString();

        if(email.length() > 0 && password.length() > 0) { //???????????? ??????????????? ???????????????
            mAuth.signInWithEmailAndPassword(email, password) //?????????
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { //????????? ?????? ???
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) { //????????? ?????? ???
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                toastMessage("????????? ??????");
                                intentActivity(MainActivity.class);
                                onBackPressed();
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                toastMessage("????????? ?????? ??????????????? ??????????????????.");
                                //updateUI(null);
                            }
                        }
                    });

        }  else {
            toastMessage("????????? ?????? ??????????????? ??????????????????.");
        }
    }

    Intent intent = getIntent();


    @Override
    public void onBackPressed() {
        finish();
    }

    private void intentActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    private void toastMessage(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }
}