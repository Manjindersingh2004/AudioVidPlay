package com.example.multimediamanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class musicPlayerActivity extends AppCompatActivity {
    int rotation=0;
    TextView txtTitle,txtTimetv,txtDuration;
    ImageView imageLogoMusicRotatable;
    SeekBar seekBar;
    AppCompatButton prev_btn,playPause_btn,next_btn,btnshare,back_btn;
    private static final int NOTIFICATION_REQUEST_CODE = 0;
    PendingIntent notificationPendingIntent;
    ArrayList<modelClassMusic> arrayList;
    musicAdapterClass adapter;
    Bitmap largeIconBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        arrayList=mediaPlayerClass.arrayList;
        txtTitle=findViewById(R.id.txtMusicTitle);
        txtTimetv=findViewById(R.id.txtTimeTvMusic);
        txtDuration=findViewById(R.id.txtDurationMusic);
        seekBar=findViewById(R.id.seekBarMusic);
        prev_btn=findViewById(R.id.btnPrevMusic);
        next_btn=findViewById(R.id.btnNextMusic);
        playPause_btn=findViewById(R.id.btnPlayPauseMusic);
        imageLogoMusicRotatable=findViewById(R.id.imgMusicLogo);
        btnshare=findViewById(R.id.btnShareMusic);
        back_btn=findViewById(R.id.btnBackMusic);
        txtTitle.setSelected(true);
        setmusicREsourse();


        musicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(mediaPlayerClass.mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayerClass.mediaPlayer.getCurrentPosition());
                    txtTimetv.setText(milisecondToTime(String.valueOf(mediaPlayerClass.mediaPlayer.getCurrentPosition())));
                }
                if(mediaPlayerClass.mediaPlayer!=null && mediaPlayerClass.mediaPlayer.isPlaying()){
                    playPause_btn.setBackgroundResource(R.drawable.pause_btn);
                    //imageLogoMusicRotatable.setRotation(rotation);
                    rotation++;
                }
                else{
                    playPause_btn.setBackgroundResource(R.drawable.play_btn);
                    rotation=0;
                   // imageLogoMusicRotatable.setRotation(rotation);
                }
                new Handler().postDelayed(this,100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayerClass.mediaPlayer!=null && fromUser){
                    mediaPlayerClass.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("audio/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse(arrayList.get(mediaPlayerClass.currentSongindex).path));
                startActivity(Intent.createChooser(shareIntent, "Share Song"));
            }
        });

        back_btn.setOnClickListener(v -> finish());

    }

    private void setmusicREsourse() {
        String title=arrayList.get(mediaPlayerClass.currentSongindex).name;
        String duration=arrayList.get(mediaPlayerClass.currentSongindex).duration;
           txtTitle.setText(title);
           txtTimetv.setText("00:00");
           txtDuration.setText(milisecondToTime(duration));
           playPause_btn.setOnClickListener(v -> playPause());
           prev_btn.setOnClickListener(v -> prevMusic());
           next_btn.setOnClickListener(v -> nextMusic());
           mediaPlayerClass.songTitle=title;
           mediaPlayerClass.songDutation=duration;
           playMusic();
           //PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
           updateNotification();


    }

    private void playMusic() {
        try {
            if(mediaPlayerClass.prevsongSongIndex!=mediaPlayerClass.currentSongindex) {
                mediaPlayerClass.mediaPlayer.reset();
                mediaPlayerClass.mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(arrayList.get(mediaPlayerClass.currentSongindex).path));
                mediaPlayerClass.mediaPlayer.prepare();
                mediaPlayerClass.mediaPlayer.start();
                mediaPlayerClass.prevsongSongIndex = mediaPlayerClass.currentSongindex;
                seekBar.setProgress(0);
            }
            mediaPlayerClass.songNotExist=0;
            seekBar.setMax(Integer.parseInt(arrayList.get(mediaPlayerClass.currentSongindex).duration));
        } catch (IOException e) {
                Toast.makeText(this, "Song not exist", Toast.LENGTH_SHORT).show();
                //txtTitle.setText("Song Not Exist");
                txtTimetv.setText("--:--");
                txtDuration.setText("--:--");
                seekBar.setProgress(0);
                mediaPlayerClass.songNotExist=1;
                mediaPlayerClass.prevsongSongIndex=-1;

           // throw new RuntimeException(e);
        }
        showNotification();

    }
    private void nextMusic() {
        if(mediaPlayerClass.currentSongindex<arrayList.size()-1){
            mediaPlayerClass.currentSongindex++;
            setmusicREsourse();
        }else{
            //Toast.makeText(this, "Already at Last Song", Toast.LENGTH_SHORT).show();
            mediaPlayerClass.currentSongindex=0;
            setmusicREsourse();
        }
    }

    private void prevMusic() {
        if(mediaPlayerClass.currentSongindex>0){
            mediaPlayerClass.currentSongindex--;
            setmusicREsourse();
        }else{
            //Toast.makeText(this, "Already at First Song", Toast.LENGTH_SHORT).show();
            mediaPlayerClass.currentSongindex=arrayList.size()-1;
            setmusicREsourse();
        }
    }

    private void playPause() {
        if(mediaPlayerClass.mediaPlayer.isPlaying()){
            mediaPlayerClass.mediaPlayer.pause();
        }
        else {
            mediaPlayerClass.mediaPlayer.start();
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


    //===============================notifocation===================================//

    private void showNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "music_player_channel")
                .setSmallIcon(R.drawable.music_logo_player)
                .setContentTitle("Music Player")
                .setContentText("Now Playing: " + arrayList.get(mediaPlayerClass.currentSongindex).name)
                .setContentIntent(notificationPendingIntent)
                .setLargeIcon(drawableToBitmap(getDrawable(R.drawable.headphones)))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Show on lock screen
                .setOngoing(true) // Keep it visible until dismissed
                .setAutoCancel(false);

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("music_player_channel",
                    "Music Player Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, builder.build());
    }


    private void updateNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "music_player_channel")
                .setSmallIcon(R.drawable.headphone_)
                .setContentTitle("Music Player")
                .setLargeIcon(drawableToBitmap(getDrawable(R.drawable.headphones)))
                .setContentText("Now Playing: " + arrayList.get(mediaPlayerClass.currentSongindex).name)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Show on lock screen
                .setOngoing(true) // Keep it visible until dismissed
                .setAutoCancel(false);

        // Set the PendingIntent for the updated notification
        builder.setContentIntent(notificationPendingIntent);

        // Show the updated notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && (intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0) {
            // This means the activity was launched from the notification
            // Handle any logic you want to execute when the activity is launched from the notification
            setIntent(intent);
        }
    }



    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


}