package com.example.music_mobile_app.model.mydatabase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Album {

    public Long id;


    public String name;


    public String image;


    public Integer popularity;


    public Boolean isActive;


}