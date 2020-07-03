package com.example.smsbroadcast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOperation extends SQLiteOpenHelper{
    public static final int database_version = 1;

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+ TableData.TableInfo.TABLE_NAME+
            "("+ TableData.TableInfo.PHONE_NUMBER+" TEXT," +TableData.TableInfo.MESSAGE_INFO+" TEXT, "+ TableData.TableInfo.DATE_OF_MESSAGE+" TEXT,"+ TableData.TableInfo.TIME_OF_MESSAGE+" TEXT );";


    public DatabaseOperation(Context context) {
        super(context, TableData.TableInfo.DATABASE_NAME, null, database_version);
        Log.d("DatabaseOperations","Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);
        Log.d("DatabaseOperations","Table Created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void putInformation(DatabaseOperation dop,String number,String message,String date,String time)
    {
        SQLiteDatabase sq = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.TableInfo.PHONE_NUMBER,number);
        cv.put(TableData.TableInfo.MESSAGE_INFO,message);
        cv.put(TableData.TableInfo.DATE_OF_MESSAGE,date);
        cv.put(TableData.TableInfo.TIME_OF_MESSAGE,time);
        long k = sq.insert(TableData.TableInfo.TABLE_NAME,null,cv);
        Log.d("DatabaseOperations","Data Inserted");
    }

    public Cursor getInformation(DatabaseOperation dop)
    {
        SQLiteDatabase sq = dop.getReadableDatabase();
        String[] columns = {TableData.TableInfo.PHONE_NUMBER, TableData.TableInfo.MESSAGE_INFO, TableData.TableInfo.DATE_OF_MESSAGE,TableData.TableInfo.TIME_OF_MESSAGE};
        Cursor cr =  sq.query(TableData.TableInfo.TABLE_NAME,columns,null,null,null,null,null);
        return cr;
    }

    public void deleteInformation(DatabaseOperation dop,String number,String message,String date,String time)
    {
        String selection = TableData.TableInfo.MESSAGE_INFO+" LIKE ? AND "+ TableData.TableInfo.PHONE_NUMBER+" LIKE ? AND "+ TableData.TableInfo.DATE_OF_MESSAGE+" LIKE ? AND "+ TableData.TableInfo.TIME_OF_MESSAGE+" LIKE ?";
        SQLiteDatabase sq = dop.getWritableDatabase();
        String args[] = {message,number,date,time};
        sq.delete(TableData.TableInfo.TABLE_NAME,selection,args);
        Log.d("DatabaseOperations","Record Deleted");
    }

}
