package com.example.myapplication.tab3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hyemin on 2020-07-13.
 */
public class ToDoDBHelper extends SQLiteOpenHelper {

    public ToDoDBHelper(Context context) {
        super(context, "Todo", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE todo(`pk` INTEGER PRIMARY KEY AUTOINCREMENT, `date` TEXT, `content` TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "drop table if exists todo";
        db.execSQL(sql);

        onCreate(db);
    }
}
