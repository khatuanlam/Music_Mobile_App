package com.example.music_mobile_app.viewmodel.mydatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.repository.mydatabase.SongRepository;

import java.util.Collections;
import java.util.List;

public class SongViewModel extends ViewModel {

    private SongRepository songRepository = new SongRepository();

    private MutableLiveData<Song> song = new MutableLiveData<>();

    public SongViewModel() {

    }

    public void loadSong(Integer id)
    {
//        songRepository.getSongById(id);
        songRepository.getSongById(id).observeForever(new Observer<Song>() {
            @Override
            public void onChanged(Song song) {
                SongViewModel.this.song.setValue(song);
            }
        });

    }
    public LiveData<Song> getSong() {
        return song;
    }
}