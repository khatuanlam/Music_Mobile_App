package com.example.music_mobile_app.model.extension;

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
    public Integer id_Db;

    public String name;

    public String image;

    public String urlLyric;

    public String urlSong;
    public String releaseDate;

    public String path;

}