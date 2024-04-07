package com.example.music_mobile_app.repository.mydatabase;

import androidx.lifecycle.LiveData;

import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.service.mydatabase.impl.SongServiceImpl;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.DeleteCallback;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.PostCallback;

import java.util.List;

public class SongRepository {

    private SongServiceImpl songService;

    public SongRepository() {
        this.songService = new SongServiceImpl();
    }

    public LiveData<List<Song>> getAllSongs() {
        return songService.getAllSongs();
    }
    public LiveData<Song> getSongById(Integer id)
    {
        return songService.getSongById(id);
    }
    public LiveData<List<Song>> getAllFavoriteSongsByUserId(long id)
    {
        return songService.getAllFavoriteSongsFromIdUser(id);
    }
    public LiveData<List<Song>> getTopPopularSongs()
    {
        return songService.getTopPopularitySongs();
    }
    public LiveData<Song> postFavoriteSongToUser(long idSong, long idUser, PostCallback postCallback){
        return songService.postFavoriteSongToUser(idSong, idUser, postCallback);
    }
    public LiveData<Song> deleteFavoriteSongByIdUser(long idSong, long idUser, DeleteCallback deleteCallback){
        return songService.deleteFavoriteSongByIdUser(idSong, idUser, deleteCallback);
    }
    public LiveData<List<Song>> getAllSongsFromAlbum(long id){
        return songService.getAllSongsFromAlbum(id);
    }
    public LiveData<List<Song>> getAllSongsFromPlaylist(long id){
        return songService.getAllSongsFromPlaylist(id);
    }

    public LiveData<List<Song>> getFilteredSongsBySongName(String songName){
        return songService.getfilteredSongs(songName);
    }

}