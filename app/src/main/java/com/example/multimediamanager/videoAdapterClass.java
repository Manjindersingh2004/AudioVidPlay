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

public class videoAdapterClass  extends RecyclerView.Adapter<videoAdapterClass.ViewHolder>{

    Context context;
    ArrayList<ModelClassVideo> arrayList;
    videoAdapterClass(Context context,ArrayList<ModelClassVideo> arrayList){
        this.arrayList=arrayList;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.music_layout_for_adapter,parent,false);

        return new videoAdapterClass.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int pos=holder.getAdapterPosition();
        holder.txtVideoLayoutName.setText(arrayList.get(position).name.toString());
        holder.imgVideoLayoutThumbnail.setImageResource(R.drawable.video_icon_recycler_view);
        holder.durationVideoLayout.setText(milisecondToTime(arrayList.get(pos).duration));


        holder.itemLayoutVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickRecyclerViewForPlayVideo(pos);
            }
        });

        holder.moremenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareVideo(Uri.parse(arrayList.get(pos).path));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtVideoLayoutName,durationVideoLayout;
        ImageView imgVideoLayoutThumbnail;
        LinearLayout itemLayoutVideo;
        AppCompatButton moremenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtVideoLayoutName=itemView.findViewById(R.id.musicLayoutName);
            imgVideoLayoutThumbnail=itemView.findViewById(R.id.musicLayoutImage);
            itemLayoutVideo=itemView.findViewById(R.id.itemLayoutMusic);
            durationVideoLayout=itemView.findViewById(R.id.durationMusicLayout);
            moremenu=itemView.findViewById(R.id.moreMusicLayout);

        }
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


    private void onclickRecyclerViewForPlayVideo(int pos) {
        Intent i= new Intent(context, videoPlayerActivity.class);
        i.putExtra("path",arrayList.get(pos).path);
        context.startActivity(i);

    }
    private void shareVideo(Uri path) {
        // Create an Intent to share the song
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("video/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM,path);
        context.startActivity(Intent.createChooser(shareIntent, "Share Video"));
    }

}
