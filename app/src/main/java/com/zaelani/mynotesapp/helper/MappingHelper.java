package com.zaelani.mynotesapp.helper;

import android.database.Cursor;

import com.zaelani.mynotesapp.db.DatabaseContract;
import com.zaelani.mynotesapp.entity.Note;

import java.util.ArrayList;

//kelas untuk mengonversi nilai cursor ke arraylist
public class MappingHelper {

    public static ArrayList<Note> mapCursorToArraylist(Cursor notesCursor){
        ArrayList<Note> noteList = new ArrayList<>();

        while (notesCursor.moveToNext()){
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE));
            String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION));
            String date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE));
            noteList.add(new Note(id, title,description,date));
        }
        return noteList;
    }
}
