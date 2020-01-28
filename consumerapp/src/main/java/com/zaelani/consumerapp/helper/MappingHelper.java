package com.zaelani.consumerapp.helper;

import android.database.Cursor;

import com.zaelani.consumerapp.db.DatabaseContract;
import com.zaelani.consumerapp.entity.Note;

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

    public static Note mapCursorToObject(Cursor noteCursor){
        noteCursor.moveToFirst();
        int id = noteCursor.getInt(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID));
        String title = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE));
        String description = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION));
        String date = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE));

        return new Note(id,title,description,date);
    }
}
