package com.example.tooset_test02;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DBHelper extends SQLiteOpenHelper {



    //DB, 테이블명, 컬럼 지정

    private Context context;
    private static final String DATABASE_NAME = "tooSet.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "tb_tooset";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "tooset_name";
    private static final String COLUMN_TYPE = "tooset_type";
    private static final String COLUMN_DATE = "tooset_date";
    private static final String COLUMN_RES_DATE = "tooset_resdate";
    private static final String COLUMN_COLOR = "tooset_color";

    DBHelper(@Nullable Context context) { //컨텍스트-정보얻기
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public DBHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //테이블 생성
        //AUTOINCREMENT : 자동 값 증가
        String query = "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_TYPE + " TEXT, " +
                        COLUMN_DATE + " DATE, " +
                        COLUMN_RES_DATE + " DATE, " +
                        COLUMN_COLOR + " TEXT );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //추가하기
    void addTooSet(String name, String type, String date, String resdate, String color) {
        SQLiteDatabase db = this.getWritableDatabase(); //읽고쓰기위해 DB 오픈
        ContentValues cv = new ContentValues(); //처리 가능한 값 집합 저장. 값 저장

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_TYPE, type);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_RES_DATE, resdate);
        cv.put(COLUMN_COLOR, color); //인자들을 각각 알맞은 컬럼명에 저장.
        long result = db.insert(TABLE_NAME, null, cv); //db에 삽입
        if(result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "추가됐습니다", Toast.LENGTH_SHORT).show();
        }
    }

    //목록
    Cursor readAllData () { //Cursor : DB에 저장된 테이블 참조해 데이터 가져옴
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    //수정하기
    public boolean updateData(String row_id, String name, String type, String date, String resdate, String color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_TYPE, type);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_RES_DATE, resdate);
        cv.put(COLUMN_COLOR, color);

        db.update(TABLE_NAME, cv, "_id=?", new String[]{ row_id });

        return true;
    }

    //삭제하기
    void deleteOneData(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1) {
            Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "삭제했습니다", Toast.LENGTH_SHORT).show();
        }
    }
}
