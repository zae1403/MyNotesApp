package com.zaelani.mynotesapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.zaelani.mynotesapp.db.NoteHelper;

import static com.zaelani.mynotesapp.db.DatabaseContract.AUTHORITY;
import static com.zaelani.mynotesapp.db.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.zaelani.mynotesapp.db.DatabaseContract.NoteColumns.TABLE_NAME;

public class NoteProvider extends ContentProvider {
   //Integer digunakan sebagai identifier antara select all sama select by id

    private static final int NOTE = 1;
    private static final int NOTE_ID = 2;
    private NoteHelper noteHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /*
   Uri matcher untuk mempermudah identifier dengan menggunakan integer
   misal
   uri com.dicoding.picodiploma.mynotesapp dicocokan dengan integer 1
   uri com.dicoding.picodiploma.mynotesapp/# dicocokan dengan integer 2
    */

    static {
        //content://com.zaelani.mynotesapp/note
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, NOTE);
        //content://com.zaelani.mynotesapp/note/id
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", NOTE_ID);
    }

    public NoteProvider() {
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        noteHelper = NoteHelper.getInstance(getContext());
        noteHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case NOTE:
                cursor = noteHelper.queryAll();
                break;
            case NOTE_ID:
                cursor = noteHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long added;
        switch (uriMatcher.match(uri)) {
            case NOTE:
                added = noteHelper.insert(values);
                break;
            default:
                added = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        int updated;
        switch (uriMatcher.match(uri)) {
            case NOTE_ID:
                updated = noteHelper.update(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int deleted;
        switch (uriMatcher.match(uri)) {
            case NOTE_ID:
                deleted = noteHelper.deleteById(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return  deleted;
    }

}
