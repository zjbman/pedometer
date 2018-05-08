package com.zjbman.pedometer.db;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * Created by Jbandxs on 2018/5/8.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String createUserTable = "create table " + "user" +
            " (_id integer primary key autoincrement,username text,password text," +
            " email text,qq text,create_date text)";

    private static final String createStepTable = "create table " + "step" +
            " (_id integer primary key autoincrement,user_id integer,step_count integer," +
            " date text)";

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MySQLiteOpenHelper(Context context) {
        super(context, "pedometer.db", null, 1, null);   //创建了名为user的数据库
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUserTable);
        db.execSQL(createStepTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

