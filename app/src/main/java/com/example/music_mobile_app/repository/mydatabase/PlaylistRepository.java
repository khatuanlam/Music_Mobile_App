package com.example.music_mobile_app.repository.mydatabase;

import androidx.lifecycle.LiveData;

import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.service.mydatabase.impl.PlaylistServiceImpl;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.DeleteCallback;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.PostCallback;

import java.util.List;

public class PlaylistRepository {

    private PlaylistServiceImpl playlistService;

    public PlaylistRepository() {
        this.playlistService = new PlaylistServiceImpl();
    }

    public LiveData<List<Playlist>> getAllPlaylists() {
        return playlistService.getAllPlaylists();
    }
    public LiveData<Playlist> getPlaylistById(long id)
    {
        return playlistService.getPlaylistById(id);
    }
     public LiveData<List<Playlist>> getAllPlaylistsByIdUser(long id)
    {
        return playlistService.getAllPlaylistsByIdUser(id);
    }
    public LiveData<Playlist> deleteSongFromPlaylist(long idPlaylist, long idSong, DeleteCallback deleteCallback)
    {
        return playlistService.deleteSongFromPlaylist(idPlaylist, idSong, deleteCallback);
    }
    public LiveData<Playlist> postSongToPlaylist(long idPlaylist, long idSong, PostCallback postCallback){
        return playlistService.postSongToPlaylist(idPlaylist, idSong, postCallback);
    }
    public LiveData<Playlist> addPlaylistToUser( long idUser, String name, PostCallback postCallback)
    {
        return playlistService.addPlaylistToUser(idUser, name, postCallback);
    }

}