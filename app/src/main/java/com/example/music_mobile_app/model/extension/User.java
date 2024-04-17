package com.example.music_mobile_app.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {

    public Long id;

    public String username;


    public String password;

    public String email;


    public Boolean vip;


    private String authorities;


    private Boolean isActive;


}