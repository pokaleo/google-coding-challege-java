package com.google;

import java.util.*;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private String playerStatus = "stopped";
  private Video currentPlaying;
  private TreeMap<String, VideoPlaylist> allPlayLists = new TreeMap<>();

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    List<Video> allVideos = videoLibrary.getVideos();
    TreeSet<String> videoList = new TreeSet<>();
    for (var video:
         allVideos) {
      videoList.add(video.getTitle()+ " (" + video.getVideoId() + ") " +
              video.getTags().toString().replace(",",""));
    }
    for (var video : videoList) {
      System.out.println(video);
    }
  }

  public void playVideo(String videoId) {
    try{
      switch (playerStatus){
        case "stopped" :
          currentPlaying = videoLibrary.getVideo(videoId);
          System.out.println("Playing video: " + currentPlaying.getTitle());
          playerStatus = "playing";
          break;
        case "playing":
        case "paused" :
          stopCurrentPlaying();
          currentPlaying = videoLibrary.getVideo(videoId);
          System.out.println("Playing video: " + currentPlaying.getTitle());
          break;
      }
    } catch (NullPointerException e) {
      System.out.println("Cannot play video: Video does not exist");
    }
  }

  public void stopVideo() {
    if(playerStatus.equals("stopped")){
      System.out.println("Cannot stop video: No video is currently playing");
    } else if(playerStatus.equals("playing")){
      stopCurrentPlaying();
      playerStatus = "stopped";
    }
  }

  public void playRandomVideo() {
    Random rand = new Random();
    Video randomVideo = videoLibrary.getVideos().get(rand.nextInt(videoLibrary.getVideos().size()));
    try{
      playVideo(randomVideo.getVideoId());
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  public void pauseVideo() {
    switch (playerStatus){
      case "playing" :
        System.out.println("Pausing video: " + currentPlaying.getTitle());
        playerStatus = "paused";
        break;
      case "paused" :
        System.out.println("Video already paused: " + currentPlaying.getTitle());
        break;
      case "stopped" :
        System.out.println("Cannot pause video: No video is currently playing");
        break;
    }
  }

  public void continueVideo() {
    switch (playerStatus){
      case "playing" :
        System.out.println("Cannot continue video: Video is not paused");
        break;
      case "paused" :
        System.out.println("Continuing video: " + currentPlaying.getTitle());
        break;
      case "stopped" :
        System.out.println("Cannot continue video: No video is currently playing");
        break;
    }
  }

  public void showPlaying() {
    switch (playerStatus){
      case "playing" :
        System.out.println("Currently playing: " + currentPlaying.getTitle()+ " (" + currentPlaying.getVideoId() + ") " +
                currentPlaying.getTags().toString().replace(",",""));
        break;
      case "paused" :
        System.out.println("Currently playing: " + currentPlaying.getTitle()+ " (" + currentPlaying.getVideoId() + ") " +
                currentPlaying.getTags().toString().replace(",","") + " - PAUSED");
        break;
      case "stopped" :
        System.out.println("No video is currently playing");
        break;
    }
  }

  public void createPlaylist(String playlistName) {
    if (!allPlayLists.containsKey(playlistName.toUpperCase())){
      System.out.println("Successfully created new playlist: " + playlistName);
      allPlayLists.put(playlistName.toUpperCase(), new VideoPlaylist(playlistName));
    } else{
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    }
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    String playListId = playlistName.toUpperCase();
    Video videoToBeAdded;
    if (!allPlayLists.containsKey(playListId)) {
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
    } else {
      try {
        videoToBeAdded = videoLibrary.getVideo(videoId);
        if (allPlayLists.get(playListId).addVideo(videoToBeAdded)){
          System.out.println("Added video to " + playlistName + ": " + videoToBeAdded.getTitle());
        } else {
          System.out.println("Cannot add video to " + playlistName+ ": Video already added");
        }
      } catch (NullPointerException e) {
        System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
      }
    }
  }

  public void showAllPlaylists() {
    if (allPlayLists.isEmpty()){
      System.out.println("No playlists exist yet");
    } else{
      System.out.println("Showing all playlists:");
      for (VideoPlaylist playlist:
           allPlayLists.values()) {
        System.out.println(playlist.getName());
      }
    }
  }

  public void showPlaylist(String playlistName) {
    if (allPlayLists.isEmpty()){
      System.out.println("Cannot show playlist "+playlistName+": Playlist does not exist");
    } else if (!allPlayLists.containsKey(playlistName.toUpperCase())){
      System.out.println("Cannot show playlist "+playlistName+": Playlist does not exist");
    }
    else{
      System.out.println("Showing playlist: "+playlistName);
      for (Video video:
      allPlayLists.get(playlistName.toUpperCase()).getVideos()) {
        System.out.println(
                video.getTitle()+ " (" + video.getVideoId() + ") " +
                        video.getTags().toString().replace(",","")
        );
      }
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    System.out.println("removeFromPlaylist needs implementation");
  }

  public void clearPlaylist(String playlistName) {
    System.out.println("clearPlaylist needs implementation");
  }

  public void deletePlaylist(String playlistName) {
    System.out.println("deletePlaylist needs implementation");
  }

  public void searchVideos(String searchTerm) {
    System.out.println("searchVideos needs implementation");
  }

  public void searchVideosWithTag(String videoTag) {
    System.out.println("searchVideosWithTag needs implementation");
  }

  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }

  public void stopCurrentPlaying(){
    System.out.println("Stopping video: " + currentPlaying.getTitle());
    playerStatus = "stopped";
  }
}