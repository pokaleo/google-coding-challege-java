package com.google;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.SortedSet;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private LinkedHashSet<Video> videos = new LinkedHashSet<>();
    private String name;

    VideoPlaylist(String name) {
        this.name = name;
    }

    protected boolean addVideo(Video video){
        if (videos.contains(video)){
            return false;
        } else {
            videos.add(video);
            return true;
        }
    }

    protected String getName(){
        return name;
    }

    protected HashSet<Video> getVideos(){
        return videos;
    }
}
