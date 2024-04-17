package com.example.music_mobile_app.viewmodel.album;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.music_mobile_app.model.Album;
import com.example.music_mobile_app.repository.AlbumRepository;

import java.util.List;

public class TopPopularAlbumViewModel extends ViewModel {

    private final AlbumRepository albumRepository = new AlbumRepository();

    private final MutableLiveData<List<Album>> albums = new MutableLiveData<>();

    public TopPopularAlbumViewModel() {
        albumRepository.getAllAlbums().observeForever(new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                TopPopularAlbumViewModel.this.albums.setValue(albums);
            }
        });
    }

    public void loadAlbum() {
        albumRepository.getAllAlbums();
    }

    public LiveData<List<Album>> getAlbums() {
        return albums;
    }
}