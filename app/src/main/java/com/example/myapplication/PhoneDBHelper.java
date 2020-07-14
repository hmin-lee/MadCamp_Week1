package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PhoneDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DiaryDBHelper";

    public PhoneDBHelper(Context context) {
        super(context, "PhoneBook", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE PhoneBook (`pk` INTEGER PRIMARY KEY AUTOINCREMENT,`icon` TEXT,`URI` TEXT,`name` TEXT,`num` TEXT);";
        Log.d(TAG, "onCreate: PhoneBook DB를 생성함");
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        String sql = "drop table if exists PhoneBook";
        db.execSQL(sql);
        onCreate(db);
    }

}
