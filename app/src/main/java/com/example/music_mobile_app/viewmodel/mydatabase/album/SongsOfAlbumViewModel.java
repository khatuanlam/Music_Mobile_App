package com.example.music_mobile_app.viewmodel.mydatabase.album;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.repository.mydatabase.PlaylistRepository;
import com.example.music_mobile_app.repository.mydatabase.SongRepository;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.DeleteCallback;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.PostCallback;

import java.util.List;

public class SongsOfAlbumViewModel extends ViewModel {

    private SongRepository songRepository = new SongRepository();

    private MutableLiveData<List<Song>> songs = new MutableLiveData<>();

    public void getAllSongsByAlbum(long id)
    {
        songRepository.getAllSongsFromAlbum(id).observeForever(new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                SongsOfAlbumViewModel.this.songs.setValue(songs);
            }
        });
    }

    public LiveData<List<Song>> getSongs() {
        return songs;
    }

}