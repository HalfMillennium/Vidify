package com.digitalnode.playsee;

public class SongInfo {
    private String name;
    private String artist;
    private String album;

    public SongInfo(String name, String artist, String album) {
        this.name = name;
        this.artist = artist;
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }
}
