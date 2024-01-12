package com.example.multimediamanager;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class musicAdapterClass extends RecyclerView.Adapter<musicAdapterClass.viewHolder> {
    Context context;
    ArrayList<modelClassMusic> arrayList;

    MainActivity a = new MainActivity();
    musicFragmentClass b = new musicFragmentClass();

    musicAdapterClass(Context context, ArrayList<modelClassMusic> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_layout_for_adapter, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.txtMusicLayoutName.setText(arrayList.get(position).name.toString());
        holder.imgMusicLayoutThumbnail.setImageResource(R.drawable.music_icon_recycler_view);   //R.drawable.music_logo_player
        holder.durationMusicLayout.setText(milisecondToTime(arrayList.get(pos).duration));

        holder.itemLayoutMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickRecyclerViewForPlayMusic(pos);
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareSong(Uri.parse(arrayList.get(pos).path));
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtMusicLayoutName, durationMusicLayout;
        ImageView imgMusicLayoutThumbnail;
        AppCompatButton share;
        LinearLayout itemLayoutMusic, MusicPlayLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtMusicLayoutName = itemView.findViewById(R.id.musicLayoutName);
            durationMusicLayout = itemView.findViewById(R.id.durationMusicLayout);
            imgMusicLayoutThumbnail = itemView.findViewById(R.id.musicLayoutImage);
            itemLayoutMusic = itemView.findViewById(R.id.itemLayoutMusic);
            MusicPlayLayout = itemView.findViewById(R.id.layoutShortMusicPlay);
            share= itemView.findViewById(R.id.moreMusicLayout);


        }
    }

    private void onclickRecyclerViewForPlayMusic(int pos) {
        if (mediaPlayerClass.prevsongSongIndex != mediaPlayerClass.currentSongindex) {
            mediaPlayerClass.getMediaPlayer_().reset();
        }
        Intent i = new Intent(context, musicPlayerActivity.class);
        new mediaPlayerClass(arrayList, pos);
        context.startActivity(i);
    }

    String milisecondToTime(String duration_) {
        Long duration = Long.parseLong(duration_);
        long HH = TimeUnit.MILLISECONDS.toHours(duration);
        long MM = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
        long SS = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;
        if(HH==0)
        return String.format("%02d:%02d",MM,SS);
        else
            return String.format("%02d:%02d:%02d",HH,MM,SS);

    }

    private void shareSong(Uri path) {
        // Create an Intent to share the song
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("audio/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, path);
        context.startActivity(Intent.createChooser(shareIntent, "Share Song"));
    }

}