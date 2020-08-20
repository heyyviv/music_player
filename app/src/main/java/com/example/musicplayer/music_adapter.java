package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class music_adapter extends RecyclerView.Adapter<music_adapter.MusicViewHolder> {
    class MusicViewHolder extends RecyclerView.ViewHolder{
        LinearLayout container;
        TextView song,artist;
        audiomodel current_song;
        int id;
        public MusicViewHolder(@NonNull final View itemView) {
            super(itemView);
            container=(LinearLayout) itemView.findViewById(R.id.container);
            song=(TextView) itemView.findViewById(R.id.music_name);
            artist=(TextView) itemView.findViewById(R.id.artist_name);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context=v.getContext();
                   Intent intent=new Intent(v.getContext(),player.class);
                   intent.putExtra("path",current_song.getPath());
                   intent.putExtra("songname",current_song.getSong());
                   intent.putExtra("id",id);
                   context.startActivity(intent);
                }
            });

        }}
    public static List<audiomodel> allsong=new ArrayList<>();
    public static void changed_song(int id,Context context){
        if(id<allsong.size() && id>=0) {
            Intent intent = new Intent(context, player.class);
            intent.putExtra("path", allsong.get(id).getPath());
            intent.putExtra("songname", allsong.get(id).getSong());
            context.startActivity(intent);
        }
    }


    public static  void fetching_songs(Context context){
        try {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST};
            Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
            if (c != null) {
                while (c.moveToNext()) {
                    audiomodel am = new audiomodel();
                    String song_path = c.getString(0);
                    String song_album = c.getString(1);
                    String song_artist = c.getString(2);
                    String song_name = song_path.substring(song_path.lastIndexOf("/") + 1);
                    am.setAlbum(song_album);
                    am.setartist(song_artist);
                    am.setPath(song_path);
                    am.setSong(song_name);
                    Log.v("musicplayer", song_name);
                    allsong.add(am);
                }
                c.close();
            }
        }catch (UnknownError e) {
            Log.v("musicplayer", "need strorage permisson");
        }
        Collections.sort(allsong);
    }

    @NonNull
    @Override
    public music_adapter.MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.musicrow,parent,false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull music_adapter.MusicViewHolder holder, int position) {
            audiomodel current=allsong.get(position);
            holder.current_song=current;
            holder.id=position;
            holder.song.setText(current.getSong());
            holder.artist.setText(current.getArtist());
    }

    @Override
    public int getItemCount() {
        return allsong.size();
    }
}
