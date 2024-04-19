package com.example.music_mobile_app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.repository.SongRepository;

import java.util.List;

public class TopPopularSongViewModel extends ViewModel {

    private SongRepository songRepository = new SongRepository();

    private MutableLiveData<List<Song>> songs = new MutableLiveData<>();

    public TopPopularSongViewModel() {
        songRepository.getTopPopularSongs().observeForever(new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                TopPopularSongViewModel.this.songs.setValue(songs);
            }
        });
    }

    public void loadSong() {
        songRepository.getTopPopularSongs();
    }

    public LiveData<List<Song>> getSongs() {
        return songs;
    }
}