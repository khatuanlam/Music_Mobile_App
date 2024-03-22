package com.example.music_mobile_app.model;

import com.google.gson.annotations.SerializedName;

public class Followers {
    @SerializedName("total")
    private Integer total;
    @SerializedName("href")
    private String href;

    public Followers(Integer total, String href) {
        this.total = total;
        this.href = href;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
