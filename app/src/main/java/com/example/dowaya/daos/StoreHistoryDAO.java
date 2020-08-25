package com.example.dowaya.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dowaya.models.Store;

import java.util.ArrayList;

public class StoreHistoryDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "store_history_4.db";
    private static final String STORE_HISTORY_TABLE_NAME = "store_history_4";
    private static final String STORE_HISTORY_ID = "id";
    private static final String STORE_HISTORY_NAME = "name";
    private static final String STORE_HISTORY_TIME = "time";

    public StoreHistoryDAO(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table "+ STORE_HISTORY_TABLE_NAME +
                        " ("+ STORE_HISTORY_ID +" text primary key, " +
                        STORE_HISTORY_NAME +" text, "+
                        STORE_HISTORY_TIME +" text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+ STORE_HISTORY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertStoreHistory(Store store) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STORE_HISTORY_ID, store.getId());
        contentValues.put(STORE_HISTORY_NAME, store.getName());
        contentValues.put(STORE_HISTORY_TIME, store.getHistoryTime());
        db.insert(STORE_HISTORY_TABLE_NAME, null, contentValues);
        return true;
    }

    public void deleteStoreHistory(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(STORE_HISTORY_TABLE_NAME,
                STORE_HISTORY_ID +" = ? ",
                new String[] {id});
    }

    public ArrayList<Store> getAllStoreHistory() {
        ArrayList<Store> storeList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ STORE_HISTORY_TABLE_NAME,
                null );
        cursor.moveToLast();
        while(!cursor.isBeforeFirst()){
            Store store = new Store();
            store.setId(cursor.getString(cursor.getColumnIndex(STORE_HISTORY_ID)));
            store.setName(cursor.getString(cursor.getColumnIndex(STORE_HISTORY_NAME)));
            store.setHistoryTime(cursor.getString(cursor.getColumnIndex(STORE_HISTORY_TIME)));
            storeList.add(store);
            cursor.moveToPrevious();
        }
        return storeList;
    }


    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, STORE_HISTORY_TABLE_NAME);
        return numRows;
    }

}