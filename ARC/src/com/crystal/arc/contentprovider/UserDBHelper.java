package com.crystal.arc.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDBHelper extends SQLiteOpenHelper {
    public UserDBHelper(Context context) {
        super(context, UserContract.DBNAME, null, UserContract.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + UserContract.TNAME + "(" +
                UserContract.TID + " integer primary key autoincrement not null," +
                UserContract.NAME + " text not null," +
                UserContract.PASSWORD + " text not null," +
                UserContract.REGISTER_DATE + " interger not null," +
                UserContract.DELETE_FLAG + " integer not null DEFAULT 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}