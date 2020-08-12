package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    music_adapter madapter;
    RecyclerView.LayoutManager mlayoutmanager;
    public static MediaPlayer mediaplayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaplayer=new MediaPlayer();
        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        mlayoutmanager=new LinearLayoutManager(this);
        madapter=new music_adapter();
        recyclerView.setLayoutManager(mlayoutmanager);
        recyclerView.setAdapter(madapter);
        music_adapter.fetching_songs(getApplicationContext());
    }

    public static void prepare_song(String location) throws IOException {
        if(mediaplayer.isPlaying()){
            mediaplayer.pause();
            mediaplayer.stop();
            mediaplayer.reset();
        }
        mediaplayer.setDataSource(location);
        mediaplayer.prepare();
    }

    public static void  song_play(){
        mediaplayer.start();
    }
    public static  void song_pause(){
        mediaplayer.pause();
    }
    public static  int get_song_position(){
        return mediaplayer.getCurrentPosition();
    }
    public static void song_seek(int pos){
        mediaplayer.seekTo(pos*1000);
    }

    public static boolean is_working(){
        return mediaplayer != null;
    }
    public static void free_mediaplayer(){
        mediaplayer.stop();
        mediaplayer.release();
    }
    public static  int get_duration(){
        return mediaplayer.getDuration();
    }
}