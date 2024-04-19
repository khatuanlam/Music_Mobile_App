package com.example.music_mobile_app.viewmodel.mydatabase.favorite;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.repository.mydatabase.SongRepository;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.DeleteCallback;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.PostCallback;

import java.util.List;

public class FavoriteSongsViewModel extends ViewModel {

    private SongRepository songRepository = new SongRepository();

    private MutableLiveData<List<Song>> songs = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDeleteSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPostSuccess = new MutableLiveData<>();

//    public long userId;

    public FavoriteSongsViewModel() {

    }
    public void getAllFavoriteSongsByUserId(long userId)
    {
        songRepository.getAllFavoriteSongsByUserId(userId).observeForever(new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                FavoriteSongsViewModel.this.songs.setValue(songs);
                Log.i("VIEW MODEL","DU LIEU CO THAY DOI");
                Log.i("VIEW MODEL",String.valueOf(songs.size()));
            }
        });
    }

    public void deleteFavoriteSongByIdUser(long idSong, long idUser) {
        try {
            songRepository.deleteFavoriteSongByIdUser(idSong, idUser, new DeleteCallback() {
                @Override
                public void onDeleteComplete() {
                    System.out.println("CHUAN BI GOI GET ALL TRONG CALLBACK");
                    getAllFavoriteSongsByUserId(idUser);
                    isDeleteSuccess.setValue(true);
                }

                @Override
                public void onDeleteFailed() {
                    isDeleteSuccess.setValue(false);
                }
            });
        } catch (Exception e) {
            isDeleteSuccess.setValue(false);
        }
    }

    public void postFavoriteSongToUser(long idSong, long idUser) {
        try {
            songRepository.postFavoriteSongToUser(idSong, idUser, new PostCallback() {
                @Override
                public void onPostComplete() {
                    System.out.println("CHUAN BI GOI GET ALL TRONG CALLBACK");
                    getAllFavoriteSongsByUserId(idUser);
                    isPostSuccess.setValue(true);
                }

                @Override
                public void onPostFailed() {
                    isPostSuccess.setValue(false);
                }
            });
        } catch (Exception e) {
            isPostSuccess.setValue(false);
        }
    }

    public LiveData<List<Song>> getSongs() {
        return songs;
    }

    public LiveData<Boolean> getIsDeleteSuccess() {
        return isDeleteSuccess;
    }

    public LiveData<Boolean> getIsPostSuccess() {
        return isPostSuccess;
    }
}