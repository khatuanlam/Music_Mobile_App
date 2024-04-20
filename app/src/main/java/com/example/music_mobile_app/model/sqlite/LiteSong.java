package com.example.music_mobile_app.model.sqlite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiteSong {

    public Integer id;
    public Integer id_mydb;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_mydb() {
        return id_mydb;
    }

    public void setId_mydb(Integer id_mydb) {
        this.id_mydb = id_mydb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrlLyric() {
        return urlLyric;
    }

    public void setUrlLyric(String urlLyric) {
        this.urlLyric = urlLyric;
    }

    public String getUrlSong() {
        return urlSong;
    }

    public void setUrlSong(String urlSong) {
        this.urlSong = urlSong;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String name;


    public String image;

    public String urlLyric;

    public String urlSong;
    public String releaseDate;

    public String path;

}