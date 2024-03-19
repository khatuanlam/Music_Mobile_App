package com.example.music_mobile_app.model;

public class Song {


    private int id;
    private int songImage;
    private String title;
    private String artistName;
    public Song(int id, int songImage, String title, String artistName) {
        this.id = id;
        this.songImage = songImage;
        this.title = title;
        this.artistName = artistName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSongImage() {
        return songImage;
    }

    public void setSongImage(int songImage) {
        this.songImage = songImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
