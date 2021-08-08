package com.google;

import java.lang.reflect.Array;
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
      if (allPlayLists.get(playlistName.toUpperCase()).isEmpty()){
        System.out.println("No videos here yet");
      } else{
        for (Video video:
                allPlayLists.get(playlistName.toUpperCase()).getVideos()) {
          System.out.println(
                  video.getTitle()+ " (" + video.getVideoId() + ") " +
                          video.getTags().toString().replace(",","")
          );
        }
      }
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    Video videoToBeRemoved = videoLibrary.getVideo(videoId);
    if (allPlayLists.isEmpty()){
      System.out.println("Cannot remove video from "+playlistName+": Playlist does not exist");
    } else if (!allPlayLists.containsKey(playlistName.toUpperCase())){
      System.out.println("Cannot remove video from "+playlistName+": Playlist does not exist");
    } else if (videoToBeRemoved == null) {
      System.out.println("Cannot remove video from "+playlistName+": Video does not exist");
    } else if (allPlayLists.get(playlistName.toUpperCase()).removeVideo(videoToBeRemoved)){
      System.out.println("Removed video from "+playlistName+": "+videoToBeRemoved.getTitle());
    } else {
      System.out.println("Cannot remove video from "+playlistName+": Video is not in playlist");
    }
  }

  public void clearPlaylist(String playlistName) {
    VideoPlaylist playlistToBeCleared = allPlayLists.get(playlistName.toUpperCase());
    if (playlistToBeCleared == null){
      System.out.println("Cannot clear playlist "+playlistName+": Playlist does not exist");
    } else {
      playlistToBeCleared.clearVideos();
      System.out.println("Successfully removed all videos from "+playlistName);
    }
  }

  public void deletePlaylist(String playlistName) {
    if (!allPlayLists.containsKey(playlistName.toUpperCase())){
      System.out.println("Cannot delete playlist "+playlistName+": Playlist does not exist");
    } else {
      allPlayLists.remove(playlistName.toUpperCase());
      System.out.println("Deleted playlist: "+playlistName);
    }
  }

  public void searchVideos(String searchTerm) {
    // Search videos from the library and store them in an arraylist
    List<Video> searchResult = new ArrayList<>();
    for (Video video:
         videoLibrary.getVideos()) {
      if(video.getTitle().toUpperCase().contains(searchTerm.toUpperCase())) {
        searchResult.add(video);
      }
    }
    // Sort the arraylist
    searchResult.sort(new Comparator<Video>() {
      @Override
      public int compare(Video video, Video t1) {
        return video.getTitle().compareToIgnoreCase(t1.getTitle());
      }
    });
    // Display the result
    displaySearchResult(searchTerm, searchResult);
  }

  public void searchVideosWithTag(String videoTag) {
    // Search videos from the library and store them in an arraylist
    List<Video> searchResult = new ArrayList<>();
    for (Video video:
            videoLibrary.getVideos()) {
      if(video.getTags().contains(videoTag.toLowerCase())) {
        searchResult.add(video);
      }
    }
    // Sort the arraylist
    searchResult.sort((video, t1) -> video.getTitle().compareToIgnoreCase(t1.getTitle()));
    // Display the result
    displaySearchResult(videoTag, searchResult);
  }

  private void displaySearchResult(String videoTag, List<Video> searchResult) {
    if (searchResult.isEmpty()){
      System.out.println("No search results for " + videoTag);
    } else {
      System.out.println("Here are the results for " + videoTag + ":");
      for (int i = 0; i < searchResult.size(); i++) {
        System.out.println((i + 1) + ") " + searchResult.get(i).getTitle() + " (" + searchResult.get(i).getVideoId() + ") " +
                searchResult.get(i).getTags().toString().replace(",", ""));
      }
      // Prompt the user to play a video from the search result
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video. \nIf your answer " +
              "is not a valid number, we will assume it's a no.");
      Scanner scanner = new Scanner(System.in);
      String input = scanner.nextLine();
      if (input.matches("[0-9]+") && (Integer.parseInt(input)-1) < searchResult.size()) {
        playVideo(searchResult.get(Integer.parseInt(input)-1).getVideoId());
      }
    }
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