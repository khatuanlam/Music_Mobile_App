package com.example.music_mobile_app.repository.mydatabase.impl;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.repository.mydatabase.SongRepository;
import com.example.music_mobile_app.retrofit.mydatabase.MyDbRetrofit;
import com.example.music_mobile_app.retrofit.mydatabase.SongService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SongRepositoryImpl implements SongRepository {

    private Retrofit retrofit;
    public SongRepositoryImpl()
    {
        this.retrofit = MyDbRetrofit.getRetrofit();
    }
    private MutableLiveData<List<Song>> songListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Song>> popular_songListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Song>> album_songListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Song>> playlist_songListLiveData = new MutableLiveData<>();
    private MutableLiveData<Song> songById = new MutableLiveData<>();

    @Override
    public LiveData<List<Song>> getAllSongs() {
        SongService songService = retrofit.create(SongService.class);
        Call<List<Song>> call = songService.getAllSongs();
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    songListLiveData.setValue(response.body());
                } else {
                    Log.i("getAllSongs","LOI");
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Log.i("getAllSongs","that bai");
                Log.i("getAllSongs",t.getMessage());
            }
        });
        return songListLiveData;
    }

    @Override
    public LiveData<List<Song>> getAllSongsFromAlbum(long id) {
        SongService songService = retrofit.create(SongService.class);
        Call<List<Song>> call = songService.getAllSongsFromAlbum(id);
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    album_songListLiveData.setValue(response.body());
                } else {
                    Log.i("getAllSongsFromAlbum","LOI");
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Log.i("getAllSongsFromAlbum","that bai");
                Log.i("getAllSongsFromAlbum",t.getMessage());
            }
        });
        return album_songListLiveData;
    }

    @Override
    public LiveData<List<Song>> getAllSongsFromPlaylist(long id) {
        SongService songService = retrofit.create(SongService.class);
        Call<List<Song>> call = songService.getAllSongsFromPlaylist(id);
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    playlist_songListLiveData.setValue(response.body());
                } else {
                    Log.i("getAllSongsFromPlaylist","LOI");
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Log.i("getAllSongsFromPlaylist","that bai");
                Log.i("getAllSongsFromPlaylist",t.getMessage());
            }
        });
        return playlist_songListLiveData;
    }

    @Override
    public LiveData<Song> getSongById(long id) {
        SongService songService = retrofit.create(SongService.class);
        Call<Song> call = songService.getSongById(id);
        call.enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                if (response.isSuccessful()) {
                    songById.setValue(response.body());
                } else {
                    Log.i("getSongById","LOI");
                }
            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {
                Log.i("getSongById","that bai");
                Log.i("getSongById",t.getMessage());
            }
        });
        return songById;
    }

    @Override
    public LiveData<List<Song>> getTopPopularitySongs() {
        SongService songService = retrofit.create(SongService.class);
        Call<List<Song>> call = songService.getTopPopularSongs(0, 10);
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    popular_songListLiveData.setValue(response.body());
                } else {
                    Log.i("getTopPopularitySongs","LOI");
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Log.i("getTopPopularitySongs","that bai");
                Log.i("getTopPopularitySongs",t.getMessage());
            }
        });
        return popular_songListLiveData;
    }
}
