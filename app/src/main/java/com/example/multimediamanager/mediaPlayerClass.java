package com.example.multimediamanager;

import android.media.MediaPlayer;
import android.net.Uri;

import java.util.ArrayList;

public class mediaPlayerClass {
    static MediaPlayer mediaPlayer;
    static ArrayList <modelClassMusic >arrayList;
    static int currentSongindex;
    static  int prevsongSongIndex=-1;
    static int songNotExist=0;
    static String songTitle="",songDutation="";



    mediaPlayerClass(ArrayList<modelClassMusic> arrayList,int currentSongindex){
        mediaPlayerClass.arrayList=arrayList;
        mediaPlayerClass.currentSongindex=currentSongindex;


    }

    public static MediaPlayer getMediaPlayer_(){
        if(mediaPlayer==null){
            mediaPlayer=new MediaPlayer();
        }
        return mediaPlayer;
    }



}
