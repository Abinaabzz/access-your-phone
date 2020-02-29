package com.example.phone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    Context context;
    public  static  final String Databasename="pcondb";


    public  static  final String Login="logintb";
    public  static final  String lcol1="id";
    public  static final String lcol2="pattern";




    public static final String  logintbcr="create table "+Login+" (id integer primary key autoincrement,pattern text)";
    public DatabaseHelper(Context context) {
        super(context, Databasename, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(logintbcr);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Login);
        onCreate(db);
    }


    public boolean reg(String pat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(lcol2,pat);

        long result = db.insert(Login,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


    public  String login(String pattern)
    {
        //String []st=new String[2];
        String id;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select id from logintb where pattern=?",new String[] {pattern});
        if(c.getCount()>0)
        {
            c.moveToFirst();
            id=(c.getInt(0))+"";
            Log.e("Tag",id);
        }
        else
        {
           id="error";
        }
        return  id;

    }


    public  Boolean changepass(String patt)
    {

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(lcol2,patt);
        long res =  db.update(Login,cv,"pattern=?",new String[]{patt});
        if(res==-1)
            return  false;
        else
            return  true;


    }
    public  String checkuser()
    {
       String s="";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM logintb", null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
           s="success";

        }
        else
        {
            s="error";
        }

        return  s;

    }


}
