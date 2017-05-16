
package iak.almuwahhid.com.iak1.Helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import iak.almuwahhid.com.iak1.kelas.Github;

/**
 * Created by gueone on 5/6/2017.
 */

public class DatabaseHandler {
    Database db;
    SQLiteDatabase database;
    Cursor csr;

    public DatabaseHandler(Context ctx) {
        this.db = new Database(ctx);
    }

    public String check_user(String a){
        String asd = "";
        try {
            database = db.getWritableDatabase();
            String query = "SELECT * FROM user WHERE username = '"+a+"'";
            csr = database.rawQuery(query, null);
            if(csr != null && !csr.isClosed()){
                while (csr.moveToNext()){
                    asd = csr.getString(csr.getColumnIndex("username"));
                }
            }
            return asd;
        }catch (Exception e){
            Log.e("error", "check_user: "+e );
            return "";
        }
    }

    public ArrayList getAllData(ArrayList<Github> e, String username){
        e = new ArrayList();
        try {
            database = db.getWritableDatabase();
//            String query = "SELECT * FROM github WHERE username = "+username;
            String query = "SELECT * FROM github";
            csr = database.rawQuery(query, null);
            while (csr.moveToNext()){
                Github github = new Github();
                github.setName_repo(csr.getString(csr.getColumnIndex("name_repo")));
                github.setDesc(csr.getString(csr.getColumnIndex("desc")));
                github.setUrl(csr.getString(csr.getColumnIndex("url")));
                github.setName_owner(csr.getString(csr.getColumnIndex("name_owner")));
                github.setPhoto(csr.getString(csr.getColumnIndex("photo")));
                e.add(github);
            }
        }catch (Exception a){
            Log.e("err", "getAllData: "+a);
        }
        return e;
    }

    public boolean addData(String a, String b, String c, String d, String e, String username){
        try {
            database = db.getWritableDatabase();
            String query = "INSERT INTO github ('name_repo', 'name_owner', 'desc', 'url', 'photo', 'username') VALUES ('"+a+"', '"+b+"', '"+c+"', '"+d+"', '"+e+"', '"+username+"')";
//            Log.e("addData", "addData: "+query );
            database.execSQL(query);
            return true;
        }catch (Exception x){
            Log.e("error", "addData: "+x );
            return false;
        }
    }
}
