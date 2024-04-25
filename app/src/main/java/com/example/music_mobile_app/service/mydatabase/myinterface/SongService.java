package com.example.music_mobile_app.service.mydatabase.myinterface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.DeleteCallback;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.PostCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Path;

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
    public LiveData<Boolean> checkFavoriteSongToUser(long idSong, long idUser);

}
