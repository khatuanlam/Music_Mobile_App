package com.example.music_mobile_app.repository.mydatabase;

import androidx.lifecycle.LiveData;

import com.example.music_mobile_app.model.mydatabase.Album;
import com.example.music_mobile_app.model.mydatabase.Song;

import java.util.List;

public interface AlbumRepository {

    LiveData<List<Album>> getAllAlbums();

    LiveData<Album> getAlbumById(long id);

    LiveData<List<Album>> getTopPopularityAlbums();
}
