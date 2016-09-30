package com.example.landy.ggchtxl2.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.landy.ggchtxl2.Model.User;
import com.example.landy.ggchtxl2.Model.UserExist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Landy on 2016/9/25.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Database";
    // Table Names
    private static final String TABLE_NAME = "UserExist";


    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // NOTES Table - column nmaes
    private static final String KEY_User = "username";
    private static final String KEY_EXIST = "exist";

    private static final String CREATE_TABLE = "CREATE TABLE "+
            TABLE_NAME +"("+KEY_ID+" INTEGER PRIMARY KEY," + KEY_User + " Text,"+
            KEY_EXIST  +" INTEGER," +KEY_CREATED_AT + " DATETIME" + ")";
    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }
    public long createUserExist(UserExist userExist)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_User,userExist.getUsername());
        values.put(KEY_EXIST,userExist.getExist());
        values.put(KEY_CREATED_AT,getDateTime());

        long userid = db.insert(TABLE_NAME,null,values);
        return userid;
    }
    public UserExist getUserExist(String key)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "select * from "+ TABLE_NAME + " where "
                +"username" +" = '" +key+"'";
        Cursor c = db.rawQuery(selectQuery,null);

        if(c!=null)
        {
            c.moveToFirst();
        }
        UserExist userExist = new UserExist();
        userExist.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        userExist.setUsername(c.getString(c.getColumnIndex(KEY_User)));
        userExist.setExist(Integer.valueOf(c.getString(c.getColumnIndex(KEY_EXIST))));
        userExist.setCreate_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return userExist;
    }
    public int updateUserExist(UserExist userExist)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EXIST,userExist.getExist());
        return db.update(TABLE_NAME,values,KEY_User+" = ?",new String[]{userExist.getUsername()});
    }
    public List<UserExist> getAll()
    {
        List<UserExist> list = new ArrayList<>();
        String selectQuery = "select * from "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        if (c.moveToFirst())
        {
            do {
                UserExist userExist = new UserExist();
                userExist.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                userExist.setUsername(c.getString(c.getColumnIndex(KEY_User)));
                userExist.setExist(Integer.valueOf(c.getString(c.getColumnIndex(KEY_EXIST))));
                userExist.setCreate_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                list.add(userExist);
            }while (c.moveToNext());
        }
        return list;
    }

    @Override
    public synchronized void close() {
        SQLiteDatabase db =this.getReadableDatabase();
        if (db!=null&&db.isOpen())
            db.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
