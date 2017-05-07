package iak.almuwahhid.com.iak1.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gueone on 5/6/2017.
 */

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "dbku", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE IF NOT EXISTS user(username TEXT PRIMARY KEY)";
        String sql2 = "CREATE TABLE IF NOT EXISTS github(id INTEGER PRIMARY KEY AUTOINCREMENT, name_repo TEXT, desc TEXT, url TEXT, name_owner TEXT, photo TEXT, username TEXT)";

        db.execSQL(sql1);
        db.execSQL(sql2);

        ContentValues val = new ContentValues();
        val.put("username", "gueone");
        db.insert("user", "username", val);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS github");
        onCreate(db);
    }




}
