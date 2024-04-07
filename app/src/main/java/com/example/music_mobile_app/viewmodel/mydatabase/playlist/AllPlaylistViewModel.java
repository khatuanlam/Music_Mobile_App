package com.example.music_mobile_app.viewmodel.mydatabase.playlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.repository.mydatabase.PlaylistRepository;
import com.example.music_mobile_app.viewmodel.mydatabase.myinterface.favorite.PostCallback;

import java.util.List;

public class AllPlaylistViewModel extends ViewModel {

    private final PlaylistRepository playlistRepository = new PlaylistRepository();

    private final MutableLiveData<List<Playlist>> playlists = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPostSuccess = new MutableLiveData<>();


    public void getAllPlaylistsByIdUser(long idUser) {
        playlistRepository.getAllPlaylistsByIdUser(idUser).observeForever(new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                AllPlaylistViewModel.this.playlists.setValue(playlists);
            }
        });
    }
    public void postFavoriteSongToUser(long idUser, String name) {
        try {
            playlistRepository.addPlaylistToUser(idUser, name, new PostCallback() {
                @Override
                public void onPostComplete() {
                    System.out.println("CHUAN BI GOI GET ALL TRONG CALLBACK");
                    getAllPlaylistsByIdUser(idUser);
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
    public LiveData<List<Playlist>> getPlaylists() {
        return playlists;
    }
    public LiveData<Boolean> getIsPostSuccess() {
        return isPostSuccess;
    }
}