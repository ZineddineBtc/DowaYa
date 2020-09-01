package com.example.dowaya.daos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dowaya.models.Medicine;

import java.util.ArrayList;

public class PostHistoryDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "post-history.db";
    private static final String POST_HISTORY_TABLE_NAME = "post-history";
    private static final String POST_HISTORY_ID = "id";
    private static final String POST_HISTORY_NAME = "name";
    private static final String POST_HISTORY_DESCRIPTION = "description";
    private static final String POST_HISTORY_PRICE = "price";
    private static final String POST_HISTORY_ADDRESS = "address";
    private static final String POST_HISTORY_PHOTO = "photo";
    private static final String POST_HISTORY_TIME = "time";

    public PostHistoryDAO(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table "+ POST_HISTORY_TABLE_NAME +
                        " ("+ POST_HISTORY_ID +" text primary key, " +
                        POST_HISTORY_NAME +" text, "+
                        POST_HISTORY_DESCRIPTION +" text, "+
                        POST_HISTORY_PRICE +" text, "+
                        POST_HISTORY_ADDRESS +" text, "+
                        POST_HISTORY_PHOTO +" text, "+
                        POST_HISTORY_TIME +" text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+ POST_HISTORY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertPostHistory(Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POST_HISTORY_ID, medicine.getId());
        contentValues.put(POST_HISTORY_NAME, medicine.getName());
        contentValues.put(POST_HISTORY_DESCRIPTION, medicine.getDescription());
        contentValues.put(POST_HISTORY_PRICE, medicine.getPriceRange());
        contentValues.put(POST_HISTORY_ADDRESS, medicine.getPostAddress());
        contentValues.put(POST_HISTORY_PHOTO, medicine.getPhoto());
        contentValues.put(POST_HISTORY_TIME, medicine.getPostTime());
        db.insert(POST_HISTORY_TABLE_NAME, null, contentValues);
        return true;
    }

    public void deletePostHistory(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(POST_HISTORY_TABLE_NAME,
                POST_HISTORY_ID +" = ? ",
                new String[] {id});
    }

    public ArrayList<Medicine> getAllPostHistory() {
        ArrayList<Medicine> medicineList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor =  db.rawQuery( "select * from "+ POST_HISTORY_TABLE_NAME,
                null );
        cursor.moveToLast();
        while(!cursor.isBeforeFirst()){
            Medicine medicine = new Medicine();
            medicine.setId(cursor.getString(cursor.getColumnIndex(POST_HISTORY_ID)));
            medicine.setName(cursor.getString(cursor.getColumnIndex(POST_HISTORY_NAME)));
            medicine.setDescription(cursor.getString(cursor.getColumnIndex(POST_HISTORY_DESCRIPTION)));
            medicine.setPrice(cursor.getString(cursor.getColumnIndex(POST_HISTORY_PRICE)));
            medicine.setPostAddress(cursor.getString(cursor.getColumnIndex(POST_HISTORY_ADDRESS)));
            medicine.setPhoto(cursor.getString(cursor.getColumnIndex(POST_HISTORY_PHOTO)));
            medicine.setPostTime(cursor.getString(cursor.getColumnIndex(POST_HISTORY_TIME)));
            medicineList.add(medicine);
            cursor.moveToPrevious();
        }
        return medicineList;
    }
    
}