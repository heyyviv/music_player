package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class player extends AppCompatActivity {
    String song_path,song_name;
    Button song_play_pause,pause;
    MediaPlayer mediaplayer;
    TextView nametv;
    SeekBar progress;
    int pos=0,cur_music=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent intent=getIntent();
        song_play_pause=findViewById(R.id.play);
        pause=findViewById(R.id.pause);
        nametv=findViewById(R.id.songname);
        progress=findViewById(R.id.song_progress);

        if(intent!=null) {
            song_path = intent.getStringExtra("path");
            song_name = intent.getStringExtra("songname");
        }
        mediaplayer=new MediaPlayer();
        nametv.setText(song_name);
        try {
            mediaplayer.setDataSource(song_path);
            mediaplayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        song_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cur_music%2==0) {
                    mediaplayer.start();
                    song_play_pause.setText("PAUSE");
                }else{
                    mediaplayer.pause();
                    song_play_pause.setText("PLAY");
                }
                cur_music++;

            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaplayer.pause();
            }
        });

        progress.setMax(mediaplayer.getDuration()/1000);
         final Handler mhandeler=new Handler();
         player.this.runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 if(mediaplayer!=null){
                     int current_pos=mediaplayer.getCurrentPosition()/1000;
                     progress.setProgress(current_pos);
                 }
                 mhandeler.postDelayed(this,1000);
             }
         });
         progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             @Override
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 if(mediaplayer!=null && fromUser){
                        mediaplayer.seekTo(progress*1000);
                 }
             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {

             }

             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {

             }
         });
    }


    @Override
    protected void onStop() {
        super.onStop();
        mediaplayer.stop();
        mediaplayer.release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("path",song_path);
        outState.putCharSequence("name",song_name);
        outState.putInt("position",mediaplayer.getCurrentPosition()/1000);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        song_path=savedInstanceState.getString("path");
        song_name=savedInstanceState.getString("name");
        pos=savedInstanceState.getInt("position");
        mediaplayer.seekTo(pos*1000);
        mediaplayer.start();
    }
}