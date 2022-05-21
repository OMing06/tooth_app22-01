package com.example.tooset_test02;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.colorpicker.model.ColorSwatch;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    EditText write_name, write_type;
    TextView edit_date, edit_result, textViewColor;
    Button btn_add, write_date, write_color;
    RadioGroup radioGroup;

    Calendar calendar;
    int nowYear, nowMonth, nowDay;
    SimpleDateFormat simpleDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        write_name = findViewById(R.id.update_name);
        write_type = findViewById(R.id.update_type);
        write_date = findViewById(R.id.update_date);
        write_color = findViewById(R.id.update_color);
        edit_date = findViewById(R.id.edit_date);
        edit_result = findViewById(R.id.d_day_View);
        btn_add = findViewById(R.id.btn_update);
        textViewColor = findViewById(R.id.textViewColor);
        radioGroup = findViewById(R.id.RadioGroup);

        calendar = Calendar.getInstance();
        nowYear = calendar.get(Calendar.YEAR);
        nowMonth = calendar.get(Calendar.MONTH);
        nowDay = calendar.get(Calendar.DAY_OF_MONTH);

        Locale.setDefault(Locale.KOREAN);

        DBHelper dbHelper = new DBHelper(AddActivity.this);


        //컬러피커 오픈
        write_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorpicker();
            }
        });

        //달력 오픈
        write_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddActivity.this, endDateSetListener, nowYear,
                        nowMonth, nowDay).show(); //오늘날짜 가져오기
            }
        });

        //추가
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbHelper.addTooSet(
                        write_name.getText().toString().trim(),
                        write_type.getText().toString().trim(),
                        edit_date.getText().toString().trim(),
                        edit_result.getText().toString().trim(),
                        textViewColor.getText().toString().trim()
                );
            }
        });
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
                        textViewColor.setBackgroundColor(color);
                        textViewColor.setText(colorHex);
                    }
                })
                .show();
    }



    private DatePickerDialog.OnDateSetListener endDateSetListener
            = new DatePickerDialog.OnDateSetListener() {

        @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            //날짜 지정
                edit_date.setText(year + "년 " + (month + 1) + "월 " + dayOfMonth  + "일");
                calendar = Calendar.getInstance();
                calendar.set(year, (month), (dayOfMonth));

                //칫솔 종류 선택에 따라 다르게 계산
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.radioBtn1) {
                        edit_date.setText(year + "년 " + (month + 1) + "월 " + dayOfMonth  + "일");
                        calendar = Calendar.getInstance();
                        calendar.set(year, (month), (dayOfMonth+79));
                        edit_result.setText(getDate());
                    }
                    else if(checkedId == R.id.radioBtn2) {
                        edit_date.setText(year + "년 " + (month + 1) + "월 " + dayOfMonth  + "일");
                        calendar = Calendar.getInstance();
                        calendar.set(year, (month), (dayOfMonth+6));
                        edit_result.setText(getDate());
                    }
                }
            });

            }
            };

    //날짜 가져오는 함수
        public String getDate() {
            simpleDate = new SimpleDateFormat("yyyy-MM-dd");
            String date = simpleDate.format(calendar.getTime());
            return date;
        }
    }