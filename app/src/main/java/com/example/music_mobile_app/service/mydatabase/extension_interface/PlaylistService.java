package com.example.music_mobile_app.service.mydatabase.extension_interface;

import androidx.lifecycle.LiveData;

import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.DeleteCallback;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.PostCallback;

import java.util.List;

public interface PlaylistService {
    public LiveData<List<Playlist>> getAllPlaylists();

    public LiveData<List<Playlist>> getAllPlaylistsByIdUser(long id);

    public LiveData<Playlist> getPlaylistById(long id);

    public LiveData<Playlist> deleteSongFromPlaylist(long idPlaylist, long idSong, DeleteCallback deleteCallback);

    public LiveData<Playlist> postSongToPlaylist(long idPlaylist, long idSong, PostCallback postCallback);

    public LiveData<Playlist> addPlaylistToUser(long idUser, String name, PostCallback postCallback);
}
