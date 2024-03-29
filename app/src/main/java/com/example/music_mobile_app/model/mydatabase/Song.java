package com.example.music_mobile_app.model.mydatabase;

import java.time.LocalDate;

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

}