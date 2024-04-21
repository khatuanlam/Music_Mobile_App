package com.example.music_mobile_app.viewmodel.mydatabase.sqlite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.model.sqlite.LiteSong;
import com.example.music_mobile_app.repository.mydatabase.SongRepository;
import com.example.music_mobile_app.repository.sqlite.LiteSongRepository;

import java.util.List;

public class DownloadSongListViewModel extends ViewModel {

    private LiteSongRepository songRepository = new LiteSongRepository(MainActivity.musicDatabaseHelper);

    private MutableLiveData<List<LiteSong>> songs = new MutableLiveData<>();

    public DownloadSongListViewModel() {
        songRepository.getAllSongs().observeForever(new Observer<List<LiteSong>>() {
            @Override
            public void onChanged(List<LiteSong> songs) {
                DownloadSongListViewModel.this.songs.setValue(songs);
            }
        });
    }

    public void loadSong()
    {
        songRepository.getAllSongs();
    }
    public LiveData<List<LiteSong>> getSongs() {
        return songs;
    }
}