package com.example.music_mobile_app.repository.mydatabase;

import androidx.lifecycle.LiveData;

import com.example.music_mobile_app.model.mydatabase.Album;
import com.example.music_mobile_app.service.mydatabase.impl.AlbumServiceImpl;
import com.example.music_mobile_app.service.mydatabase.impl.SongServiceImpl;

import java.util.List;

public class AlbumRepository {

    private AlbumServiceImpl albumService;

    public AlbumRepository() {
        this.albumService = new AlbumServiceImpl();
    }

    public LiveData<List<Album>> getAllAlbums() {
        return albumService.getAllAlbums();
    }
    public LiveData<Album> getAlbumById(Integer id)
    {
        return albumService.getAlbumById(id);
    }
    public LiveData<List<Album>> getTopPopularAlbums()
    {
        return albumService.getTopPopularityAlbums();
    }

}