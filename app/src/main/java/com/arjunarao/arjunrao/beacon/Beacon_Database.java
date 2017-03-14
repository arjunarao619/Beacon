package com.arjunarao.arjunrao.beacon;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Arjun Rao on 1/17/2017.
 */

public class Beacon_Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "BEACON_DATABASE";
    private static final int DB_VERSION = 1;

    Beacon_Database(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String email_table= "CREATE TABLE USEREMAIL(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "USER_EMAIL TEXT);";
        db.execSQL(email_table);


        String table_contacts_create = "CREATE TABLE CONTACTS(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT, "
                + "NUMBER TEXT);";
        db.execSQL(table_contacts_create);

      String emergency = "CREATE TABLE OPTIONS(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
              "ISLOCATION INTEGER, "
                + "ISFOLLOW INTEGER, "
              + "ISRECORD INTEGER);";

        db.execSQL(emergency);

        ContentValues values = new ContentValues();
        values.put("ISLOCATION",0);
        values.put("ISFOLLOW",0);
        values.put("ISRECORD",0);

        db.insert("OPTIONS",null,values);











    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
