package com.example.dowaya.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dowaya.models.Medicine;

import java.util.ArrayList;

public class MedicineHistoryDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "medicine-history.db";
    private static final String MEDICINE_HISTORY_TABLE_NAME = "medicine-history";
    private static final String MEDICINE_HISTORY_ID = "id";
    private static final String MEDICINE_HISTORY_NAME = "name";
    private static final String MEDICINE_HISTORY_TIME = "time";

    public MedicineHistoryDAO(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table "+ MEDICINE_HISTORY_TABLE_NAME +
                        " ("+ MEDICINE_HISTORY_ID +" text primary key, " +
                        MEDICINE_HISTORY_NAME +" text, "+
                        MEDICINE_HISTORY_TIME +" text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+ MEDICINE_HISTORY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertMedicineHistory(Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDICINE_HISTORY_ID, medicine.getId());
        contentValues.put(MEDICINE_HISTORY_NAME, medicine.getName());
        contentValues.put(MEDICINE_HISTORY_TIME, medicine.getSearchHistoryTime());
        db.insert(MEDICINE_HISTORY_TABLE_NAME, null, contentValues);
        return true;
    }

    public ArrayList<Medicine> getAllMedicineHistory() {
        ArrayList<Medicine> medicineList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ MEDICINE_HISTORY_TABLE_NAME,
                null );
        cursor.moveToLast();
        while(!cursor.isBeforeFirst()){
            Medicine medicine = new Medicine();
            medicine.setId(cursor.getString(cursor.getColumnIndex(MEDICINE_HISTORY_ID)));
            medicine.setName(cursor.getString(cursor.getColumnIndex(MEDICINE_HISTORY_NAME)));
            medicine.setSearchHistoryTime(
                    cursor.getString(cursor.getColumnIndex(MEDICINE_HISTORY_TIME)));
            medicineList.add(medicine);
            cursor.moveToPrevious();
        }
        return medicineList;
    }

    
    
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, MEDICINE_HISTORY_TABLE_NAME);
        return numRows;
    }
    
}