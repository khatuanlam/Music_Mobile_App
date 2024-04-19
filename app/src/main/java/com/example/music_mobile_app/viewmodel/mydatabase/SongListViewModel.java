package com.example.music_mobile_app.viewmodel.mydatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.repository.mydatabase.SongRepository;

import java.util.Collections;
import java.util.List;

public class SongListViewModel extends ViewModel {

    private SongRepository songRepository = new SongRepository();

    private MutableLiveData<List<Song>> songs = new MutableLiveData<>();

    public SongListViewModel() {
        songRepository.getAllSongs().observeForever(new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                SongListViewModel.this.songs.setValue(songs);
            }
        });
    }

    public void loadSong()
    {
        songRepository.getAllSongs();
    }
    public LiveData<List<Song>> getSongs() {
        return songs;
    }
}