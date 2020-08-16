package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class player extends AppCompatActivity {
    String song_path,song_name;
    Button song_play_pause,song_next,song_prev;
    ImageView cover_img;
    TextView nametv,str_time,end_time;
    SeekBar progress;
    int pos=0,cur_music=0;
    int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent intent=getIntent();
        song_play_pause=findViewById(R.id.song_play);
        song_prev=findViewById(R.id.song_prev);
        song_next=findViewById(R.id.song_next);
        cover_img=findViewById(R.id.image_frame);
        nametv=findViewById(R.id.songname);
        progress=findViewById(R.id.song_progress);
        str_time=findViewById(R.id.start_time);
        end_time=findViewById(R.id.end_time);

        if(intent!=null) {
            song_path = intent.getStringExtra("path");
            song_name = intent.getStringExtra("songname");
            id=intent.getIntExtra("id",0);
        }

        nametv.setText(song_name);
        try {
            MainActivity.prepare_song(song_path,song_name,id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        song_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.is_playing()) {
                    MainActivity.song_play();
                    //song_play_pause.setText("PAUSE");
                    song_play_pause.setBackgroundResource(R.drawable.pause);
                }else{
                    MainActivity.song_pause();
                    //song_play_pause.setText("PLAY");
                    song_play_pause.setBackgroundResource(R.drawable.play);
                }

            }
        });
        song_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kk = id - 1;
                if (kk > 0) {
                    MainActivity.free_mediaplayer();
                    Intent intent = new Intent(v.getContext(), player.class);
                    intent.putExtra("path", music_adapter.allsong.get(kk).getPath());
                    intent.putExtra("songname", music_adapter.allsong.get(kk).getSong());
                    intent.putExtra("id",kk);
                    finish();
                    startActivity(intent);
                }
            }
        });
        song_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kk = id + 1;
                if (kk <music_adapter.allsong.size()) {
                    MainActivity.free_mediaplayer();
                    Intent intent = new Intent(v.getContext(), player.class);
                    intent.putExtra("path", music_adapter.allsong.get(kk).getPath());
                    intent.putExtra("songname", music_adapter.allsong.get(kk).getSong());
                    intent.putExtra("id",kk);
                    finish();
                    startActivity(intent);

                }
            }
        });
        restoring_image(song_path);
        progress.setMax(MainActivity.get_duration()/1000);
        int[] time_end=MainActivity.time_format(MainActivity.get_duration());
        end_time.setText(time_end[0]+":"+time_end[1]);
         final Handler mhandeler=new Handler();
         player.this.runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 if(MainActivity.is_working()){
                     int current_pos=MainActivity.get_song_position()/1000;
                     int[] cur_time=MainActivity.time_format(current_pos*1000);
                     str_time.setText(cur_time[0]+":"+cur_time[1]);
                     progress.setProgress(current_pos);
                 }
                 mhandeler.postDelayed(this,1000);
             }
         });
         progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             @Override
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 if(MainActivity.is_working() && fromUser){
                        MainActivity.song_seek(progress);
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


    /*@Override
    protected void onStop() {
        super.onStop();
        MainActivity.free_mediaplayer();
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("path",song_path);
        outState.putCharSequence("name",song_name);
        outState.putInt("position",MainActivity.get_song_position()/1000);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        song_path=savedInstanceState.getString("path");
        song_name=savedInstanceState.getString("name");
        pos=savedInstanceState.getInt("position");
        MainActivity.song_seek(pos);
        MainActivity.song_play();
    }
    public  void restoring_image(String location){

            android.media.MediaMetadataRetriever mData=new MediaMetadataRetriever();
            mData.setDataSource(location);
            byte art[]=mData.getEmbeddedPicture();
            if(art!=null) {
                Bitmap image = BitmapFactory.decodeByteArray(art, 0, art.length);
                cover_img.setImageBitmap(image);
            }

    }
}