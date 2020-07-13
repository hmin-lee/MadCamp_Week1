package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jongwow on 2020-07-13.
 */
public class DiaryDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DiaryDBHelper";

    public DiaryDBHelper(Context context) {
        super(context, "Diary", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE diary (`pk` INTEGER PRIMARY KEY AUTOINCREMENT,`date` TEXT,`content` TEXT);";
        Log.d(TAG, "onCreate: DiaryDB를 생성함");
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        String sql = "drop table if exists diary";
        db.execSQL(sql);

        onCreate(db);
    }


}
