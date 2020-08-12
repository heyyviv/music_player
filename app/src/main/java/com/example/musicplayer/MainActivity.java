package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    music_adapter madapter;
    RecyclerView.LayoutManager mlayoutmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        mlayoutmanager=new LinearLayoutManager(this);
        madapter=new music_adapter();
        recyclerView.setLayoutManager(mlayoutmanager);
        recyclerView.setAdapter(madapter);
        music_adapter.fetching_songs(getApplicationContext());
    }
}