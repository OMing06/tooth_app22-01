package com.example.tooset_test02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    Button temhum_button, review_button, user_button, search_button;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db;
    FirebaseUser user;
    UserInfo profile;
    private static final String TAG = "MainActivity";

    DBHelper dbHelper;
    ArrayList<String> tooset_id, tooset_name, tooset_type, tooset_date, tooset_resdate, tooset_color;
    TooSetAdapter tooSetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if(user == null) { //로그인 돼 있지 않으면
            intentFlagActivity(LoginActivity.class);
        } else if(!user.isEmailVerified()){
            intentFlagActivity(EmailVerifyActivity.class);
        }
        else { //로그인하면
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if(document != null) {
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            } else {
                                Log.d(TAG, "No such document");
                                //intentFlagActivity(UserAddActivity.class);

                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });



        }

        setTitle(":0");

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        temhum_button = findViewById(R.id.temhum_button);
        review_button = findViewById(R.id.review_button);
        user_button = findViewById(R.id.user_button);
        search_button = findViewById(R.id.search_button);



        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        temhum_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, TemHumActivity.class);
                startActivity(intent2);
            }
        });

        review_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this, ReviewActivity.class);
                startActivity(intent3);
            }
        });

        user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(MainActivity.this, UserAddActivity.class);
                startActivity(intent5);
            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent6);
                Toast.makeText(MainActivity.this, "Comming soon!", Toast.LENGTH_SHORT).show();
            }
        });






        dbHelper = new DBHelper(MainActivity.this);
        tooset_id = new ArrayList<>();
        tooset_name = new ArrayList<>();
        tooset_type = new ArrayList<>();
        tooset_date = new ArrayList<>();
        tooset_resdate = new ArrayList<>();
        tooset_color = new ArrayList<>();

        ViewDataArrays();

        //리사이클러뷰에 어댑터(카드 한 장) 적용 //어댑터는 꾸며주는 역할. 데이터를 리사이클러뷰에 뿌려주면 어댑터가 작동해 데이터를 지정한 위치에 배치한다.
        tooSetAdapter = new TooSetAdapter(MainActivity.this, this, tooset_id, tooset_name, tooset_type, tooset_date, tooset_resdate, tooset_color);
        recyclerView.setAdapter(tooSetAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    } //onCreate(Bundle savedInstanceState) 끝

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            recreate();
        }
    }

    void ViewDataArrays() { //db데이터 가져옴
        Cursor cursor = dbHelper.readAllData();
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                tooset_id.add(cursor.getString(0));
                tooset_name.add(cursor.getString(1));
                tooset_type.add(cursor.getString(2));
                tooset_date.add(cursor.getString(3));
                tooset_resdate.add(cursor.getString(4));
                tooset_color.add(cursor.getString(5));
            }
        }
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

    private void intentFlagActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}