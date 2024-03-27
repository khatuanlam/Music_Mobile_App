package com.example.music_mobile_app.manager;


import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class ListManager {

    private static ListManager listManager;

    private List<Track> trackLists;

    private List<Track> recentlyTracks;

    private List<Track> recommendTracks;

    private List<Track> topTracks;


    public static ListManager getInstance() {
        if (listManager == null) {
            listManager = new ListManager();
        }
        return listManager;
    }

    public ListManager() {
        trackLists = new ArrayList<>();
        recentlyTracks = new ArrayList<>();
        recommendTracks = new ArrayList<>();
        topTracks = new ArrayList<>();
    }

    public static void setListManager(ListManager listManager) {
        ListManager.listManager = listManager;
    }

    public List<Track> getTopTracks() {
        return topTracks;
    }

    public void setTopTracks(List<Track> topTracks) {
        this.topTracks = topTracks;
    }

    public List<Track> getTrackLists() {
        return trackLists;
    }

    public void setTrackLists(List<Track> trackLists) {
        this.trackLists = trackLists;
    }

    public List<Track> getRecentlyTracks() {
        return recentlyTracks;
    }

    public void setRecentlyTracks(List<Track> recentlyTracks) {
        this.recentlyTracks = recentlyTracks;
    }

    public List<Track> getRecommendTracks() {
        return recommendTracks;
    }

    public void setRecommendTracks(List<Track> recommendTracks) {
        this.recommendTracks = recommendTracks;
    }
}
