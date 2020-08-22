package com.example.dowaya.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dowaya.models.Medicine;
import com.example.dowaya.models.Store;

import java.util.ArrayList;

public class MedicineHistoryDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "medicine_history.db";
    private static final String MEDICINE_HISTORY_TABLE_NAME = "medicine_history";
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
                        " ("+ MEDICINE_HISTORY_ID +" integer primary key, " +
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

    public boolean insertMedicineHistory(String name, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDICINE_HISTORY_NAME, name);
        contentValues.put(MEDICINE_HISTORY_TIME, time);
        db.insert(MEDICINE_HISTORY_TABLE_NAME, null, contentValues);
        return true;
    }

    public void deleteMedicineHistory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MEDICINE_HISTORY_TABLE_NAME,
                MEDICINE_HISTORY_ID +" = ? ",
                new String[] { Integer.toString(id) });
        ContentValues contentValues = new ContentValues();
        for(int i=id; i<=numberOfRows(); i++){
            contentValues.put(MEDICINE_HISTORY_ID, i);
            db.update(MEDICINE_HISTORY_TABLE_NAME, contentValues, MEDICINE_HISTORY_ID +" = ? ",
                    new String[] { Integer.toString(i+1) } );
        }

    }

    public ArrayList<String[]> getAllMedicineHistory() {
        ArrayList<String[]> medicineList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ MEDICINE_HISTORY_TABLE_NAME,
                null );
        cursor.moveToLast();
        while(!cursor.isBeforeFirst()){
            String[] str = new String[2];
            str[0] = cursor.getString(cursor.getColumnIndex(MEDICINE_HISTORY_NAME));
            str[1] = cursor.getString(cursor.getColumnIndex(MEDICINE_HISTORY_TIME));
            medicineList.add(str);
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