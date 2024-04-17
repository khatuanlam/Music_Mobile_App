package com.example.music_mobile_app.repository.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "song";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ID_MYDB = "id_mydb";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_URL_SONG = "urlSong";
    private static final String COLUMN_URL_LYRIC = "urlLyric";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_RELEASE_DATE = "releaseDate";
    private static final String COLUMN_PATH = "path";

    public MusicDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ID_MYDB + " INTEGER, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_IMAGE + " TEXT, "
                + COLUMN_URL_SONG + " TEXT, "
                + COLUMN_URL_LYRIC + " TEXT, "
                + COLUMN_RELEASE_DATE +" TEXT,"
                + COLUMN_PATH + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

}