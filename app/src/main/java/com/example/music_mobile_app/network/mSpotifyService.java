package com.example.music_mobile_app.network;

import com.example.music_mobile_app.model.mydatabase.Album;
import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.model.mydatabase.User;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface mSpotifyService {

    @PUT("me")
    Call<Void> updateUserProfile(
            @Header("Authorization") String authToken,
            @Body User user
    );

    // Các API cho phần extension trong ứng dụng
    @GET("/Album")
    Call<List<Album>> getAllAlbums();

    @GET("/Album/{id}")
    Call<Album> getAlbumById(@Path("id") long id);

    @GET("/Album/Top")
    Call<List<Album>> getTopPopularAlbums(@Query("page") int page, @Query("size") int size);

    @GET("/Playlist")
    Call<List<Playlist>> getAllPlaylists();

    @PUT("playlists/{playlist_id}/images")
    Call<Void> uploadPlaylistImage(
            @Header("Authorization") String authorization,
            @Path("playlist_id") String playlistId,
            @Body RequestBody image
    );

    @DELETE("/Playlist/DeleteSong/{idPlaylist}/{idSong}")
    Call<Playlist> deleteSongFromPlaylist(@Path("idPlaylist") long idPlaylist, @Path("idSong") long idSong);

    @POST("/Playlist/AddSong/{idPlaylist}/{idSong}")
    Call<Playlist> postSongToPlaylist(@Path("idPlaylist") long idPlaylist, @Path("idSong") long idSong);

    //    @POST("/Playlist/{idUser}")
//    Call<Playlist> addPlaylistToUser(@Path("idUser") long idUser, @Body AddPlaylistBody addPlaylistBody);
    @GET("/Playlist")
    Call<List<Playlist>> getAllPlaylistsByIdUser(@Query("idUser") long id);

    @GET("/Playlist/{id}")
    Call<Playlist> getPlaylistById(@Path("id") long id);

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

    @GET("/Song/TopPopularity")
    Call<List<Song>> getTopPopularSongs(@Query("page") int page, @Query("size") int size);

    @GET("/Song/Filter")
    Call<List<Song>> getfilteredSongs(@Query("songName") String songName);

}
