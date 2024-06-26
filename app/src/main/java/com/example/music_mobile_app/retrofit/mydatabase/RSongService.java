package com.example.music_mobile_app.retrofit.mydatabase;

import com.example.music_mobile_app.model.mydatabase.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RSongService {
    @GET("/Song")
    Call<List<Song>> getAllSongs();

    @GET("/Song/{id}")
    Call<Song> getSongById(@Path("id") long id);

    @GET("/Song/Album/{id}")
    Call<List<Song>> getAllSongsFromAlbum(@Path("id") long id);

    @GET("/Song/Playlist/{id}")
    Call<List<Song>> getAllSongsFromPlaylist(@Path("id") long id);

    @GET("/Song/Favorite/{id}")
    Call<List<Song>> getAllFavoriteSongsFromIdUser(@Path("id") long id);
    @POST("/Song/AddFavorite/{idSong}/{idUser}")
    Call<Song> postFavoriteSongToUser(@Path("idSong") long idSong, @Path("idUser") long idUser);

    @DELETE("/Song/DeleteFavorite/{idSong}/{idUser}")
    Call<Song> deleteFavoriteSongByIdUser(@Path("idSong") long idSong, @Path("idUser") long idUser);

    @GET("/Song/CheckFavorite/{idSong}/{idUser}")
    Call<Boolean> checkFavoriteSongToUser(@Path("idSong") long idSong, @Path("idUser") long idUser);
    @GET("/Song/TopPopularity")
    Call<List<Song>> getTopPopularSongs(@Query("page") int page, @Query("size") int size);

    @GET("/Song/Filter")
    Call<List<Song>> getfilteredSongs(@Query("songName") String songName);

}
