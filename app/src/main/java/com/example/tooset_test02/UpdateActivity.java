package com.example.tooset_test02;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.colorpicker.model.ColorSwatch;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity {

    TextView edit_date2, edit_result2, edit_color;
    EditText update_name, update_type;
    Button btn_date, btn_color, btn_update, btn_delete;
    RadioGroup radioGroup2;

    String id, name, type, date, resdate, color;

    Calendar calendar;
    int nowYear, nowMonth, nowDay;
    boolean now;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        calendar = Calendar.getInstance();
        nowYear = calendar.get(Calendar.YEAR);
        nowMonth = calendar.get(Calendar.MONTH);
        nowDay = calendar.get(Calendar.DAY_OF_MONTH);

        edit_date2 = findViewById(R.id.edit_date2);
        edit_result2 = findViewById(R.id.d_day_View2);
        update_name = findViewById(R.id.update_name);
        update_type = findViewById(R.id.update_type);
        btn_color = findViewById(R.id.update_color2);
        btn_date = findViewById(R.id.btn_date2);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        edit_color = findViewById(R.id.textViewColor);
        radioGroup2 = findViewById(R.id.RadioGroup2);

        getAndSetIntentData();

        btn_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorpicker();
            }
        });

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateActivity.this, endDateSetListener, nowYear,
                        nowMonth, nowDay).show();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog_update();

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });


        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioBtn33) {
                    edit_result2.setText("사용중지");
                    Toast.makeText(UpdateActivity.this, name + "을 사용중지합니다. 후에 수정해주세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(name);


        }







    void getAndSetIntentData() {
        if(getIntent().hasExtra("id") &&
                getIntent().hasExtra("name") &&
                getIntent().hasExtra("type") &&
                getIntent().hasExtra("date") &&
                getIntent().hasExtra("resdate") &&
                getIntent().hasExtra("color")
        ) {

            //Intent에서 데이터 가져옴
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            type = getIntent().getStringExtra("type");
            date = getIntent().getStringExtra("date");
            resdate = getIntent().getStringExtra("resdate");
            color = getIntent().getStringExtra("color");

            //Intent에 데이터 세팅함
            update_name.setText(name);
            update_type.setText(type);
            edit_date2.setText(date);
            edit_result2.setText(resdate);
            edit_color.setText(color);
            edit_color.setBackgroundColor(Color.parseColor(color));
        }


        else { }
    }

    void confirmDialog_update() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(name + "을 수정하시겠습니까?");
        builder.setMessage("수정 후 복구되지 않습니다.");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBHelper dbHelper = new DBHelper(UpdateActivity.this);

                name = update_name.getText().toString().trim();
                type = update_type.getText().toString().trim();
                date = edit_date2.getText().toString().trim();
                resdate = edit_result2.getText().toString().trim();
                color = edit_color.getText().toString().trim();
                dbHelper.updateData(id, name, type, date, resdate, color);

                finish();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        builder.create().show();
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("정말 " + name + "을 삭제하시겠습니까?");
        builder.setMessage("삭제 후 복구되지 않습니다.");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBHelper dbHelper = new DBHelper(UpdateActivity.this);
                dbHelper.deleteOneData(id);
                finish();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        builder.create().show();
    }


    public void openColorpicker() {
        int mDefaultColor = 0;
        new MaterialColorPickerDialog
                .Builder(this)
                .setTitle("색상 선택")
                .setColorShape(ColorShape.SQAURE)
                .setColorSwatch(ColorSwatch._300)
                .setDefaultColor(mDefaultColor)
                .setColorListener(new ColorListener() {
                    @Override
                    public void onColorSelected(int color, @NotNull String colorHex) {
                        edit_color.setBackgroundColor(color);
                        edit_color.setText(colorHex);
                    }
                })
                .show();
    }

    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edit_date2.setText(year + "년 " + (month + 1) + "월 " + dayOfMonth + "일");
            calendar = Calendar.getInstance();
            calendar.set(year, (month), (dayOfMonth));
            radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.radioBtn11) {
                        edit_date2.setText(year + "년 " + (month + 1) + "월 " + dayOfMonth + "일");
                        calendar = Calendar.getInstance();
                        calendar.set(year, (month), (dayOfMonth + 79));
                        edit_result2.setText(getDate());
                    }
                    else if (checkedId == R.id.radioBtn22) {
                        edit_date2.setText(year + "년 " + (month + 1) + "월 " + dayOfMonth + "일");
                        calendar = Calendar.getInstance();
                        calendar.set(year, (month), (dayOfMonth + 6));
                        edit_result2.setText(getDate());
                    }
                }
            }); //라디오 끝

        }
    };

    public String getDate() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDate.format(calendar.getTime());
        return date;
    }


}