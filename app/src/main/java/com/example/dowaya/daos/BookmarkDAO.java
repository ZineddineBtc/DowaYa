package com.example.dowaya.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dowaya.models.Medicine;

import java.util.ArrayList;

public class BookmarkDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "0.db";
    private static final String MEDICINE_TABLE_NAME = "medicine";
    private static final String MEDICINE_ID = "id";
    private static final String MEDICINE_NAME = "name";
    private static final String MEDICINE_DESCRIPTION = "description";
    private static final String MEDICINE_PRICE = "price";

    public BookmarkDAO(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table "+ MEDICINE_TABLE_NAME +
                        " ("+ MEDICINE_ID +" integer primary key, " +
                        MEDICINE_NAME +" text, "+
                        MEDICINE_DESCRIPTION +" text, "+
                        MEDICINE_PRICE +" text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+ MEDICINE_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertMedicine (Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDICINE_NAME, medicine.getName());
        contentValues.put(MEDICINE_DESCRIPTION, medicine.getDescription());
        contentValues.put(MEDICINE_PRICE, medicine.getPriceRange());
        db.insert(MEDICINE_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateMedicine (int id, Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDICINE_NAME, medicine.getName());
        contentValues.put(MEDICINE_DESCRIPTION, medicine.getDescription());
        contentValues.put(MEDICINE_PRICE, medicine.getPriceRange());
        db.update(MEDICINE_TABLE_NAME, contentValues, MEDICINE_ID +" = ? ",
                new String[] { Integer.toString(id) } );
        return true;
    }

    public void deleteMedicine(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MEDICINE_TABLE_NAME,
                MEDICINE_ID +" = ? ",
                new String[] { Integer.toString(id) });
        ContentValues contentValues = new ContentValues();
        for(int i=id; i<=numberOfRows(); i++){
            contentValues.put(MEDICINE_ID, i);
            db.update(MEDICINE_TABLE_NAME, contentValues, MEDICINE_ID +" = ? ",
                    new String[] { Integer.toString(i+1) } );
        }

    }

    public Medicine getMedicine(int profileID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ MEDICINE_TABLE_NAME +
                " where " + MEDICINE_ID + "=" +profileID, null );
        cursor.moveToFirst();
        Medicine medicine = new Medicine(
                cursor.getString(cursor.getColumnIndex(MEDICINE_NAME)),
                cursor.getString(cursor.getColumnIndex(MEDICINE_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(MEDICINE_PRICE)));
        cursor.close();
        return medicine;
    }

    public ArrayList<Medicine> getAllMedicines() {
        ArrayList<Medicine> medicinesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ MEDICINE_TABLE_NAME,
                null );
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Medicine medicine = new Medicine(
                    cursor.getInt(cursor.getColumnIndex(MEDICINE_ID)),
                    cursor.getString(cursor.getColumnIndex(MEDICINE_NAME)),
                    cursor.getString(cursor.getColumnIndex(MEDICINE_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(MEDICINE_PRICE)));
            medicinesList.add(medicine);
            cursor.moveToNext();
        }
        return medicinesList;
    }

    public ArrayList<Medicine> getAllMedicinesReversed() {
        ArrayList<Medicine> medicinesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ MEDICINE_TABLE_NAME,
                null );
        cursor.moveToLast();
        while(!cursor.isBeforeFirst()){
            Medicine medicine = new Medicine(
                    cursor.getInt(cursor.getColumnIndex(MEDICINE_ID)),
                    cursor.getString(cursor.getColumnIndex(MEDICINE_NAME)),
                    cursor.getString(cursor.getColumnIndex(MEDICINE_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(MEDICINE_PRICE)));
            medicinesList.add(medicine);
            cursor.moveToPrevious();
        }
        return medicinesList;
    }
    public ArrayList<String> getAllNames() {
        ArrayList<String> medicinesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ MEDICINE_TABLE_NAME,
                null );
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            medicinesList.add(cursor.getString(cursor.getColumnIndex(MEDICINE_NAME)));
            cursor.moveToNext();
        }
        return medicinesList;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, MEDICINE_TABLE_NAME);
        return numRows;
    }

    public boolean contains(String medicineName){
        boolean inserted = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ MEDICINE_TABLE_NAME,
                null );
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            if(medicineName.equals(cursor.getString(cursor.getColumnIndex(MEDICINE_NAME)))){
                inserted = true;
            }
            cursor.moveToNext();
        }
        return inserted;
    }
}