package com.example.music_mobile_app.service.mydatabase.impl;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.retrofit.mydatabase.MyDbRetrofit;
import com.example.music_mobile_app.retrofit.mydatabase.RSongService;
import com.example.music_mobile_app.service.mydatabase.myinterface.SongService;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.DeleteCallback;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.PostCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SongServiceImpl implements SongService {

    private final RSongService RSongService;

    public SongServiceImpl() {
        Retrofit retrofit = MyDbRetrofit.getRetrofit();
        RSongService = retrofit.create(RSongService.class);
    }

    @Override
    public MutableLiveData<List<Song>> getAllSongs() {
        final MutableLiveData<List<Song>> songs = new MutableLiveData<>();
        Call<List<Song>> call = RSongService.getAllSongs();
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    songs.setValue(response.body());
                } else {
                    Log.i("getAllSongs", "LOI");
                    songs.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Log.i("getAllSongs", "that bai");
                Log.i("getAllSongs", t.getMessage());
                songs.setValue(null);
            }
        });
        return songs;
    }

    @Override
    public LiveData<List<Song>> getAllSongsFromAlbum(long id) {
        final MutableLiveData<List<Song>> liveData = new MutableLiveData<>();
        Call<List<Song>> call = RSongService.getAllSongsFromAlbum(id);
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    Log.i("getAllSongsFromAlbum", "LOI");
                    liveData.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Log.i("getAllSongsFromAlbum", "that bai");
                Log.i("getAllSongsFromAlbum", t.getMessage());
                liveData.setValue(null);

            }
        });
        return liveData;
    }

    @Override
    public LiveData<List<Song>> getAllSongsFromPlaylist(long id) {
        final MutableLiveData<List<Song>> liveData = new MutableLiveData<>();
        Call<List<Song>> call = RSongService.getAllSongsFromPlaylist(id);
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    Log.i("getAllSongsFromPlaylist", "LOI");
                    liveData.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Log.i("getAllSongsFromPlaylist", "that bai");
                Log.i("getAllSongsFromPlaylist", t.getMessage());
                liveData.setValue(null);
            }
        });
        return liveData;
    }

    @Override
    public LiveData<Song> getSongById(long id) {
        final MutableLiveData<Song> song = new MutableLiveData<>();
        Call<Song> call = RSongService.getSongById(id);
        call.enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                if (response.isSuccessful()) {
                    song.setValue(response.body());
                } else {
                    Log.i("getSongById", "LOI");
                    song.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {
                Log.i("getSongById", "that bai");
                Log.i("getSongById", t.getMessage());
                song.setValue(null);
            }
        });
        return song;
    }

    @Override
    public LiveData<List<Song>> getTopPopularitySongs() {
        final MutableLiveData<List<Song>> songs = new MutableLiveData<>();

        Call<List<Song>> call = RSongService.getTopPopularSongs(0, 10);
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    songs.setValue(response.body());
                } else {
                    Log.i("getTopPopularitySongs", "LOI");
                    songs.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Log.i("getTopPopularitySongs", "that bai");
                Log.i("getTopPopularitySongs", t.getMessage());
                songs.setValue(null);
            }
        });
        return songs;
    }

    @Override
    public LiveData<List<Song>> getAllFavoriteSongsFromIdUser(long idUser) {
        final MutableLiveData<List<Song>> songs = new MutableLiveData<>();

        Call<List<Song>> call = RSongService.getAllFavoriteSongsFromIdUser(idUser);
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    songs.setValue(response.body());
                    List<Song> songs1 = songs.getValue();
                    Log.i("SONGS1", "START");
                    songs1.forEach(s -> System.out.println(s.toString()));
                } else {
                    Log.i("GET ALL SONGS", "LOI");
                    songs.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Log.i("GET ALL SONGS", "that bai");
                Log.i("GET ALL SONGS", t.getMessage());
                songs.setValue(null);
            }
        });
        return songs;
    }

    @Override
    public LiveData<Song> postFavoriteSongToUser(long idSong, long idUser, PostCallback postCallback) {
        final MutableLiveData<Song> song = new MutableLiveData<>();

        Call<Song> call = RSongService.postFavoriteSongToUser(idSong, idUser);
        call.enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                if (response.isSuccessful()) {
                    song.setValue(response.body());
                    if (postCallback != null) {
                        postCallback.onPostComplete();
                    }
                } else {
                    Log.i("postFavoriteSongToUser", "LOI");
                    song.setValue(null);
                    if (postCallback != null) {
                        postCallback.onPostFailed();
                    }
                }

            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {
                Log.i("postFavoriteSongToUser", "that bai");
                Log.i("postFavoriteSongToUser", t.getMessage());
                song.setValue(null);
                if (postCallback != null) {
                    postCallback.onPostFailed();
                }
            }
        });
        return song;
    }

    @Override
    public LiveData<Song> deleteFavoriteSongByIdUser(long idSong, long idUser, DeleteCallback deleteCallback) {
        final MutableLiveData<Song> song = new MutableLiveData<>();

        Call<Song> call = RSongService.deleteFavoriteSongByIdUser(idSong, idUser);
        call.enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                if (response.isSuccessful()) {
                    song.setValue(response.body());
                    // Song song1 = song.getValue();
                    // Log.i("SONGS DELETE","START");
                    // System.out.println(song1.toString());
                    if (deleteCallback != null) {
                        deleteCallback.onDeleteComplete();
                    }
                } else {
                    Log.i("DELETE", "LOI");
                    song.setValue(null);
                    if (deleteCallback != null) {
                        deleteCallback.onDeleteFailed();
                    }
                }

            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {
                Log.i("DELETE", "that bai");
                Log.i("DELETE", t.getMessage());
                song.setValue(null);
                if (deleteCallback != null) {
                    deleteCallback.onDeleteFailed();
                }
            }
        });
        return song;
    }

    @Override
    public LiveData<List<Song>> getfilteredSongs(String songName) {
        final MutableLiveData<List<Song>> songs = new MutableLiveData<>();

        Call<List<Song>> call = RSongService.getfilteredSongs(songName);
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    songs.setValue(response.body());
                } else {
                    songs.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {

                songs.setValue(null);
            }
        });
        return songs;
    }

    @Override
    public LiveData<Boolean> checkFavoriteSongToUser(long idSong, long idUser) {
        final MutableLiveData<Boolean> result = new MutableLiveData<>();

        Call<Boolean> call = RSongService.checkFavoriteSongToUser(idSong, idUser);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful())
                    result.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                result.setValue(null);
            }
        });
        return result;
    }
}
