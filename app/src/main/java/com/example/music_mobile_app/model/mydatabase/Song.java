package com.example.music_mobile_app.model.mydatabase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Song {

    public Long id;

    public String name;


    public String image;

    public String urlLyric;

    public String urlSong;

    public Integer popularity;

    public String releaseDate;

    public Boolean isActive;

    public String artist;

    public String imageArtist;


    public Song(String name, String artist, String imageTrack, String imageArtist, String urlSong) {
        this.name = name;
        this.artist = artist;
        this.image = imageTrack;
        this.imageArtist = imageArtist;
        this.urlSong = urlSong;
    }

}