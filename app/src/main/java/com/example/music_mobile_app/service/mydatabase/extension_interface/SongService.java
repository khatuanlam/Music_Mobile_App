package com.example.music_mobile_app.service.mydatabase.extension_interface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.music_mobile_app.model.extension.Song;
import com.example.music_mobile_app.viewmodel.favorite.DeleteCallback;
import com.example.music_mobile_app.viewmodel.favorite.PostCallback;

import java.util.List;

public interface SongService {
    public MutableLiveData<List<Song>> getAllSongs();

    public LiveData<List<Song>> getAllSongsFromAlbum(long id);

    public LiveData<List<Song>> getAllSongsFromPlaylist(long id);

    public LiveData<Song> getSongById(long id);

    public LiveData<List<Song>> getTopPopularitySongs();

    public LiveData<List<Song>> getAllFavoriteSongsFromIdUser(long idUser);

    public LiveData<Song> postFavoriteSongToUser(long idSong, long idUser, PostCallback postCallback);

    public LiveData<Song> deleteFavoriteSongByIdUser(long idSong, long idUser, DeleteCallback deleteCallback);

    public LiveData<List<Song>> getfilteredSongs(String songName);
}
