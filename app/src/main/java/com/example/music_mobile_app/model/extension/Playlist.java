package com.example.music_mobile_app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
// tự động tạo các phương thức getter, setter, equals(), hashCode(), và toString() cho tất cả các trường của lớp.

@NoArgsConstructor
// tạo ra một constructor không có tham số,

@AllArgsConstructor
// tạo ra một constructor chứa tất cả các tham số của lớp,

@Builder
// tạo ra một builder pattern cho lớp
public class Playlist {

    public Long id;

    public String name;


    public String createDate;


    public Boolean isActive;


}