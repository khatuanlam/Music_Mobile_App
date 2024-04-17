package com.example.music_mobile_app.service.mydatabase.extension_interface;

import androidx.lifecycle.LiveData;

import com.example.music_mobile_app.model.Album;

import java.util.List;

public interface AlbumService {

    public LiveData<List<Album>> getAllAlbums();

    public LiveData<Album> getAlbumById(long id);

    public LiveData<List<Album>> getTopPopularityAlbums();
}
