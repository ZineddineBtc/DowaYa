package com.example.dowaya.daos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dowaya.models.Medicine;

import java.util.ArrayList;

public class RequestHistoryDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "requestHistory.db";
    private static final String REQUEST_HISTORY_TABLE_NAME = "requestHistory";
    private static final String REQUEST_HISTORY_ID = "id";
    private static final String REQUEST_HISTORY_NAME = "name";
    private static final String REQUEST_HISTORY_DESCRIPTION = "description";
    private static final String REQUEST_HISTORY_PHOTO = "photo";
    private static final String REQUEST_HISTORY_DOSE = "dose";
    private static final String REQUEST_HISTORY_TIME = "time";

    public RequestHistoryDAO(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table "+ REQUEST_HISTORY_TABLE_NAME +
                        " ("+ REQUEST_HISTORY_ID +" text primary key, " +
                        REQUEST_HISTORY_NAME +" text, "+
                        REQUEST_HISTORY_DESCRIPTION +" text, "+
                        REQUEST_HISTORY_PHOTO +" text, "+
                        REQUEST_HISTORY_DOSE +" text, "+
                        REQUEST_HISTORY_TIME +" text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+ REQUEST_HISTORY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertRequestHistory(Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REQUEST_HISTORY_ID, medicine.getId());
        contentValues.put(REQUEST_HISTORY_NAME, medicine.getName());
        contentValues.put(REQUEST_HISTORY_DESCRIPTION, medicine.getDescription());
        contentValues.put(REQUEST_HISTORY_PHOTO, medicine.getPhoto());
        contentValues.put(REQUEST_HISTORY_DOSE, medicine.getDose());
        contentValues.put(REQUEST_HISTORY_TIME, medicine.getRequestTime());
        db.insert(REQUEST_HISTORY_TABLE_NAME, null, contentValues);
        return true;
    }

    public void deleteRequestHistory(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(REQUEST_HISTORY_TABLE_NAME,
                REQUEST_HISTORY_ID +" = ? ",
                new String[] {id});
    }

    public ArrayList<Medicine> getAllRequestHistory() {
        ArrayList<Medicine> medicineList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor =  db.rawQuery( "select * from "+ REQUEST_HISTORY_TABLE_NAME,
                null );
        cursor.moveToLast();
        while(!cursor.isBeforeFirst()){
            Medicine medicine = new Medicine();
            medicine.setId(cursor.getString(cursor.getColumnIndex(REQUEST_HISTORY_ID)));
            medicine.setName(cursor.getString(cursor.getColumnIndex(REQUEST_HISTORY_NAME)));
            medicine.setDescription(cursor.getString(cursor.getColumnIndex(REQUEST_HISTORY_DESCRIPTION)));
            medicine.setPhoto(cursor.getString(cursor.getColumnIndex(REQUEST_HISTORY_PHOTO)));
            medicine.setDose(cursor.getString(cursor.getColumnIndex(REQUEST_HISTORY_DOSE)));
            medicine.setRequestTime(cursor.getString(cursor.getColumnIndex(REQUEST_HISTORY_TIME)));
            medicineList.add(medicine);
            cursor.moveToPrevious();
        }
        return medicineList;
    }
}