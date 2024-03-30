package com.example.music_mobile_app.repository.mydatabase;

import androidx.lifecycle.LiveData;

import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.model.mydatabase.Song;

import java.util.List;

public interface PlaylistRepository {

    LiveData<List<Playlist>> getAllPlaylists();


    LiveData<Playlist> getPlaylistById(long id);

}
