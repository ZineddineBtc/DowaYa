package com.example.dowaya.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dowaya.models.Medicine;

import java.util.ArrayList;

public class BookmarkDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookmark-history.db";
    private static final String MEDICINE_TABLE_NAME = "bookmark-history";
    private static final String MEDICINE_ID = "id";
    private static final String MEDICINE_NAME = "name";
    private static final String MEDICINE_DESCRIPTION = "description";
    private static final String MEDICINE_PRICE = "price";
    private static final String MEDICINE_DOSE = "dose";

    public BookmarkDAO(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table "+ MEDICINE_TABLE_NAME +
                        " ("+ MEDICINE_ID +" text primary key, " +
                        MEDICINE_NAME +" text, "+
                        MEDICINE_DESCRIPTION +" text, "+
                        MEDICINE_PRICE +" text, "+
                        MEDICINE_DOSE +" text)"
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
        contentValues.put(MEDICINE_ID, medicine.getId());
        contentValues.put(MEDICINE_NAME, medicine.getName());
        contentValues.put(MEDICINE_DESCRIPTION, medicine.getDescription());
        contentValues.put(MEDICINE_PRICE, medicine.getPriceRange());
        contentValues.put(MEDICINE_DOSE, medicine.getDose());

        db.insert(MEDICINE_TABLE_NAME, null, contentValues);
        return true;
    }

    public void deleteMedicine(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MEDICINE_TABLE_NAME,
                MEDICINE_ID +" = ? ",
                new String[] {id});
    }

    public ArrayList<Medicine> getAllMedicines() {
        ArrayList<Medicine> medicinesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ MEDICINE_TABLE_NAME,
                null );
        cursor.moveToLast();
        while(!cursor.isBeforeFirst()){
            Medicine medicine = new Medicine();
            medicine.setId(cursor.getString(cursor.getColumnIndex(MEDICINE_ID)));
            medicine.setName(cursor.getString(cursor.getColumnIndex(MEDICINE_NAME)));
            medicine.setDescription(cursor.getString(cursor.getColumnIndex(MEDICINE_DESCRIPTION)));
            medicine.setPrice(cursor.getString(cursor.getColumnIndex(MEDICINE_PRICE)));
            medicine.setDose(cursor.getString(cursor.getColumnIndex(MEDICINE_DOSE)));
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