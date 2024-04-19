package com.example.music_mobile_app.viewmodel.mydatabase.song;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.repository.mydatabase.SongRepository;

import java.util.List;

public class FilteredSongsViewModel extends ViewModel {

    private SongRepository songRepository = new SongRepository();

    private MutableLiveData<List<Song>> songs = new MutableLiveData<>();

    public void getFilteredSongsBySongName(String songName) {
        songRepository.getFilteredSongsBySongName(songName).observeForever(new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                FilteredSongsViewModel.this.songs.setValue(songs);
            }
        });
    }

    public LiveData<List<Song>> getSongs() {
        return songs;
    }
}