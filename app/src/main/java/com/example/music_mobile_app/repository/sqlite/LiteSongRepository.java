package com.example.music_mobile_app.repository.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.model.sqlite.LiteSong;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LiteSongRepository {
    private static final String TABLE_NAME = "song";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ID_MYDB = "id_mydb";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_URL_SONG = "urlSong";
    private static final String COLUMN_URL_LYRIC = "urlLyric";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_RELEASE_DATE = "releaseDate";
    private static final String COLUMN_PATH = "path";

    private MusicDatabaseHelper musicDatabaseHelper;

    public LiteSongRepository(MusicDatabaseHelper musicDatabaseHelper) {
        this.musicDatabaseHelper = musicDatabaseHelper;
    }

    public void insertSong(Song song, String path) {
        SQLiteDatabase db = musicDatabaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, song.getName());
        values.put(COLUMN_ID_MYDB, song.getId());
        values.put(COLUMN_PATH, path);
        Log.i("INSERT", path);
        values.put(COLUMN_IMAGE, song.getImage());
        values.put(COLUMN_URL_SONG, song.getUrlSong());
        values.put(COLUMN_URL_LYRIC, song.getUrlLyric());
        values.put(COLUMN_RELEASE_DATE, song.getReleaseDate());

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public LiveData<List<LiteSong>> getAllSongs() {
        MutableLiveData<List<LiteSong>> songsLiveData = new MutableLiveData<>();
        ArrayList<LiteSong> songs = new ArrayList<>();
        SQLiteDatabase db = musicDatabaseHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int idMyDb = cursor.getInt(1);
                String name = cursor.getString(2);
                String image = cursor.getString(3);
                String urlSong = cursor.getString(4);
                String urlLyric = cursor.getString(5);
                String releaseDate = cursor.getString(6);
                String path = cursor.getString(7);
                Log.i("GET :", path);
                LiteSong song = new LiteSong(id, idMyDb, name, image, urlLyric, urlSong, releaseDate, path);
                songs.add(song);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        songsLiveData.setValue(songs);
        return songsLiveData;
    }

    public LiveData<LiteSong> getSongByPath(String path) {
        MutableLiveData<LiteSong> songLiveData = new MutableLiveData<>();
        SQLiteDatabase db = musicDatabaseHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PATH + " = ?";
        String[] selectionArgs = { path };

        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        LiteSong song = null;

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            int idMyDb = cursor.getInt(1);
            String name = cursor.getString(2);
            String image = cursor.getString(3);
            String urlSong = cursor.getString(4);
            String urlLyric = cursor.getString(5);
            String releaseDate = cursor.getString(6);

            song = new LiteSong(id, idMyDb, name, image, urlLyric, urlSong, releaseDate, path);
        }

        Log.i("FUCK MY LIFE", song.toString());
        cursor.close();
        db.close();
        songLiveData.setValue(song);
        return songLiveData;
    }

}
