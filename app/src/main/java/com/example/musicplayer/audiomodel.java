package com.example.musicplayer;

public class audiomodel implements  Comparable<audiomodel>{
    String song,artist,album,path;
    void setSong(String tsong){this.song=tsong;}
    String getSong(){return song;}

    void setartist(String tartist){this.artist=tartist;}
    String getArtist(){return artist;}

    void setAlbum(String talbum){this.album=talbum;}
    String getAlbum(){return album;}

    void setPath(String tpath){this.path=tpath;}
    String getPath(){return path;}

    @Override
    public int compareTo(audiomodel o) {
        return this.song.compareTo(o.getSong());
    }

}
