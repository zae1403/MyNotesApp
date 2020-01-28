package com.zaelani.mynotesapp.db;

import android.provider.BaseColumns;

//kelas CONTRAT agar mempermudah akses nama tabel dan kolom di dalam db kita
public class DatabaseContract {

    static String TABLE_NAME = "note";

    public static final class NoteColumns implements BaseColumns{
        public static String TITLE = "title";
        public static String DESCRIPTION = "description";
        public static String DATE = "date";
    }
}
