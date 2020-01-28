package com.zaelani.mynotesapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.zaelani.mynotesapp.db.DatabaseContract.TABLE_NAME;

public class NoteHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper databaseHelper;
    private static NoteHelper INSTANCE;

    private static SQLiteDatabase database;

    public NoteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    //method untuk menginisiasi database
    public static NoteHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new NoteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    //method untuk membuka dan menutup koneksi database
    public void open() throws SQLException{
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();

        if (database.isOpen()){
            database.close();
        }
    }

    //buat method untuk melakukan proses CRUD
    //method pertama untuk mengambil data
    public Cursor queryAll(){
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC");
    }

    //method mengambil data dengan ID tertentu
    public Cursor queryById(String id){
        return database.query(
                DATABASE_TABLE,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    //method untuk menyimpan data
    public long insert(ContentValues values){
        return database.insert(DATABASE_TABLE, null, values);
    }

    //method untuk memperbarui data
    public int update(String id, ContentValues values){
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    //method untuk menghapus data
    public int deleteById(String id){
        return database.delete(DATABASE_TABLE, _ID+ " = ?", new String[]{id});
    }
}
