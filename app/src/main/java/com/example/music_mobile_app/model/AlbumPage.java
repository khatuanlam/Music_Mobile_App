package com.example.music_mobile_app.model;


import com.example.music_mobile_app.model.Album;
public class AlbumPage {
    public Page<Album> albumPage;

    public Page<Album> getAlbumPage() {
        return albumPage;
    }

    public void setAlbumPage(Page<Album> albumPage) {
        this.albumPage = albumPage;
    }

    public AlbumPage(Page<Album> albumPage) {
        this.albumPage = albumPage;
    }
}

