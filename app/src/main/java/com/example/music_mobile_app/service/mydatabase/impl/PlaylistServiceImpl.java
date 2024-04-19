package com.example.music_mobile_app.service.mydatabase.impl;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.retrofit.mydatabase.MyDbRetrofit;
import com.example.music_mobile_app.retrofit.mydatabase.RPlaylistService;
import com.example.music_mobile_app.retrofit.mydatabase.model.AddPlaylistBody;
import com.example.music_mobile_app.service.mydatabase.myinterface.PlaylistService;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.DeleteCallback;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.PostCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlaylistServiceImpl implements PlaylistService {
    private final RPlaylistService RPlaylistService;

    public PlaylistServiceImpl() {
        Retrofit retrofit = MyDbRetrofit.getRetrofit();
        RPlaylistService = retrofit.create(RPlaylistService.class);
    }

    @Override
    public LiveData<List<Playlist>> getAllPlaylists() {
        final MutableLiveData<List<Playlist>> playlists = new MutableLiveData<>();
        Call<List<Playlist>> call = RPlaylistService.getAllPlaylists();
        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()) {
                    playlists.setValue(response.body());
                } else {
                    Log.i("getAllPlaylists", "LOI");
                    playlists.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.i("getAllPlaylists", "that bai");
                Log.i("getAllPlaylists", t.getMessage());
                playlists.setValue(null);
            }
        });
        return playlists;
    }

    @Override
    public LiveData<List<Playlist>> getAllPlaylistsByIdUser(long id) {
        final MutableLiveData<List<Playlist>> playlists = new MutableLiveData<>();

        Call<List<Playlist>> call = RPlaylistService.getAllPlaylistsByIdUser(id);
        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()) {
                    playlists.setValue(response.body());
                } else {
                    Log.i("getAllPlaylistsByIdUser", "LOI");
                    playlists.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.i("getAllPlaylistsByIdUser", "that bai");
                Log.i("getAllPlaylistsByIdUser", t.getMessage());
                playlists.setValue(null);
            }
        });
        return playlists;
    }

    @Override
    public LiveData<Playlist> getPlaylistById(long id) {
        final MutableLiveData<Playlist> playlist = new MutableLiveData<>();

        Call<Playlist> call = RPlaylistService.getPlaylistById(id);
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    playlist.setValue(response.body());
                } else {
                    Log.i("getPlaylistById", "LOI");
                    playlist.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                Log.i("getPlaylistById", "that bai");
                Log.i("getPlaylistById", t.getMessage());
                playlist.setValue(null);
            }
        });
        return playlist;
    }

    @Override
    public LiveData<Playlist> deleteSongFromPlaylist(long idPlaylist, long idSong, DeleteCallback deleteCallback) {
        final MutableLiveData<Playlist> playlist = new MutableLiveData<>();

        Call<Playlist> call = RPlaylistService.deleteSongFromPlaylist(idPlaylist, idSong);
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    playlist.setValue(response.body());
                    if (deleteCallback != null) {
                        deleteCallback.onDeleteComplete();
                    }
                } else {
                    Log.i("deleteSongFromPlaylist", "LOI");
                    playlist.setValue(null);
                    if (deleteCallback != null) {
                        deleteCallback.onDeleteFailed();
                    }
                }

            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                Log.i("deleteSongFromPlaylist", "that bai");
                Log.i("deleteSongFromPlaylist", t.getMessage());
                if (deleteCallback != null) {
                    deleteCallback.onDeleteFailed();
                }
            }
        });
        return playlist;
    }

    @Override
    public LiveData<Playlist> postSongToPlaylist(long idPlaylist, long idSong, PostCallback postCallback) {
        final MutableLiveData<Playlist> playlist = new MutableLiveData<>();

        Call<Playlist> call = RPlaylistService.postSongToPlaylist(idPlaylist, idSong);
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    playlist.setValue(response.body());
                    if (postCallback != null) {
                        postCallback.onPostComplete();
                    }
                } else {
                    Log.i("postSongToPlaylist", "LOI");
                    playlist.setValue(null);
                    if (postCallback != null) {
                        postCallback.onPostFailed();
                    }
                }

            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                Log.i("postSongToPlaylist", "that bai");
                Log.i("postSongToPlaylist", t.getMessage());
                playlist.setValue(null);
                if (postCallback != null) {
                    postCallback.onPostFailed();
                }

            }
        });
        return playlist;
    }

    @Override
    public LiveData<Playlist> addPlaylistToUser(long idUser, String name, PostCallback postCallback) {
        final MutableLiveData<Playlist> playlist = new MutableLiveData<>();

        Call<Playlist> call = RPlaylistService.addPlaylistToUser(idUser, new AddPlaylistBody(name));
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    playlist.setValue(response.body());
                    if (postCallback != null) {
                        postCallback.onPostComplete();
                    }
                } else {
                    playlist.setValue(null);
                    if (postCallback != null) {
                        postCallback.onPostFailed();
                    }
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                playlist.setValue(null);
                if (postCallback != null) {
                    postCallback.onPostFailed();
                }

            }
        });
        return playlist;
    }
}
