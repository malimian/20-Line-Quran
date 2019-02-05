package com.hatinco.quran20lines.a20linequran;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Fazal  Rehman on 7/27/2018.
 */

public class blc extends SQLiteOpenHelper {

    Context context;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "QuranBookMarksDB";

    public blc(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // SQL statement to create book table
        // SQL statement to create book table
        String CREATE_BOOKMARKS_TABLE = "CREATE TABLE bookmarks ( " +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "pagneo INTEGER, "+
                "title  VARCHAR(100) "+
                ")";

        db.execSQL(CREATE_BOOKMARKS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void insert_in_bookmarks(int pageno , String title){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO bookmarks VALUES(NULL,'"+pageno+"','"+title+"');");
    }


    public ArrayList<HashMap<String, String>> bookmarks_list()
    {
        general_funtions gf_ = new general_funtions();
        ArrayList<HashMap<String, String>> product_list = new  ArrayList<>();

        String sql = "SELECT * FROM bookmarks";
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery(sql, null);

        if(c.moveToFirst())
        {
            do
                        {
//                                    "title",
//                                    "pageno",
//                                    "sura_pages"

                HashMap<String, String>  pageno_ = new HashMap<>();
                String title = c.getString(2);
                int pageno = c.getInt(1);
                            pageno_.put("title",title);
                            pageno_.put("pageno", String.valueOf(pageno));
                product_list.add(pageno_);
            }

            while(c.moveToNext());
        }

        return product_list;
    }

}
