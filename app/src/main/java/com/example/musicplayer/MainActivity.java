package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    music_adapter madapter;
    RecyclerView.LayoutManager mlayoutmanager;
    public static MediaPlayer mediaplayer;
    Button play, previous, next;
    LinearLayout container;
    public static TextView songTV;
    public static int song_position = 0, song_id=0;
    public static String song_location=" ", song_name=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        songTV = findViewById(R.id.song_name);
        container = findViewById(R.id.miniplayer);
        mediaplayer = new MediaPlayer();
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        mlayoutmanager = new LinearLayoutManager(this);
        madapter = new music_adapter();
        recyclerView.setLayoutManager(mlayoutmanager);
        recyclerView.setAdapter(madapter);
        music_adapter.fetching_songs(getApplicationContext());
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kk = song_id - 1;
                if (kk > 0) {
                    free_mediaplayer();
                    Intent intent = new Intent(getApplicationContext(), player.class);
                    intent.putExtra("path", music_adapter.allsong.get(kk).getPath());
                    intent.putExtra("songname", music_adapter.allsong.get(kk).getSong());
                    intent.putExtra("id",kk);
                    startActivity(intent);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kk = song_id + 1;
                Log.e("kkid",String.valueOf(kk));
                if (kk <music_adapter.allsong.size()) {
                    free_mediaplayer();
                    Intent intent = new Intent(v.getContext(), player.class);
                    intent.putExtra("path", music_adapter.allsong.get(kk).getPath());
                    Log.e("kkid",music_adapter.allsong.get(kk).getPath());
                    intent.putExtra("songname", music_adapter.allsong.get(kk).getSong());

                    intent.putExtra("id",kk);
                    startActivity(intent);
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!is_playing()) {
                    song_play();
                    //song_play_pause.setText("PAUSE");
                    play.setBackgroundResource(R.drawable.pause);
                } else {
                    song_pause();
                    //song_play_pause.setText("PLAY");
                    play.setBackgroundResource(R.drawable.play);
                }
            }
        });
        try {
            info_gathering();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void info_gathering() throws IOException {
        SharedPreferences sd=getSharedPreferences("musicplayer",MODE_PRIVATE);
        song_location=sd.getString("songpath","");
        song_name=sd.getString("songname","");
        song_id=sd.getInt("id",0);
        if(song_location!="") {
            prepare_song(song_location, song_name, song_id);
            songTV.setText(song_name);
        }
    }



    public static void prepare_song(String location, String name, int position) throws IOException {
        if(mediaplayer==null){mediaplayer=new MediaPlayer();}
            mediaplayer.pause();
            mediaplayer.stop();
            mediaplayer.reset();

        song_location = location;
        song_name = name;
        song_id = position;
        songTV.setText(song_name);
        mediaplayer.setDataSource(location);
        mediaplayer.prepare();

    }

    public static boolean is_playing(){
        return mediaplayer.isPlaying();
    }

    public static void song_play() {
        mediaplayer.start();
    }

    public static void song_pause() {
        mediaplayer.pause();
    }

    public static int get_song_position() {
        return mediaplayer.getCurrentPosition();
    }

    public static void song_seek(int pos) {
        mediaplayer.seekTo(pos * 1000);
    }

    public static boolean is_working() {
        return mediaplayer != null;
    }

    public static void free_mediaplayer() {
        mediaplayer.stop();
        mediaplayer.reset();
    }

    public static int get_duration() {
        return mediaplayer.getDuration();
    }

    public static int[] time_format(int time) {
        int ss = time / 1000;
        int mm = ss / 60;
        ss = ss - (60 * mm);
        return new int[]{mm, ss};
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaplayer.release();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(mediaplayer!=null && mediaplayer.isPlaying()){
            play.setBackgroundResource(R.drawable.pause);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sh= getSharedPreferences("musicplayer",MODE_PRIVATE);
        SharedPreferences.Editor editor=sh.edit();
        editor.putString("songpath",song_location);
        editor.putString("songname",song_name);
        editor.putInt("id",song_id);
        editor.apply();
    }
}