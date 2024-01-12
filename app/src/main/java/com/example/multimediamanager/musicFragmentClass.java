package com.example.multimediamanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link musicFragmentClass#newInstance} factory method to
 * create an instance of this fragment.
 */
public class musicFragmentClass extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public musicFragmentClass() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment musicFragmentClass.
     */
    // TODO: Rename and change types and number of parameters
    public static musicFragmentClass newInstance(String param1, String param2) {
        musicFragmentClass fragment = new musicFragmentClass();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    RecyclerView musicRecyclerView;
    int rotation=0;
    LinearLayout layoutShortMusicPlay;
    AppCompatButton btnPlayPauseShort;
    TextView txtTitleShortMusic,txtTimeTvShort;
    SeekBar seekBarShort;
    ArrayList<modelClassMusic> arrayListMusic;
    musicAdapterClass adapter;
    ImageView rotation_logo;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_music_class, container, false);
        musicRecyclerView=view.findViewById(R.id.music_recyclerView);
        layoutShortMusicPlay=view.findViewById(R.id.layoutShortMusicPlay);
        btnPlayPauseShort=view.findViewById(R.id.btnplayPauseShort);
        txtTitleShortMusic=view.findViewById(R.id.txtMusicTitleShort);
        txtTimeTvShort=view.findViewById(R.id.txtTimeTvShort);
        seekBarShort=view.findViewById(R.id.seekbarShort);
        rotation_logo=view.findViewById(R.id.musicLogoShort);
        txtTitleShortMusic.setSelected(true);
        arrayListMusic=new ArrayList<>();
        musicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            readMusic();
        if(arrayListMusic.size()==0)
            Toast.makeText(getContext(), "No music found on your device", Toast.LENGTH_LONG).show();


        seekBarShort.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayerClass.mediaPlayer!=null&& fromUser){
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
        btnPlayPauseShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayerClass.mediaPlayer.isPlaying())
                    mediaPlayerClass.mediaPlayer.pause();
                else
                    mediaPlayerClass.mediaPlayer.start();
            }
        });
        layoutShortMusicPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),musicPlayerActivity.class));
            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayerClass.mediaPlayer!=null) {
                    try {
                        txtTitleShortMusic.setText(mediaPlayerClass.songTitle);
                        seekBarShort.setMax(Integer.parseInt(mediaPlayerClass.songDutation));
                    }catch (Exception e){

                    }
                    seekBarShort.setProgress(mediaPlayerClass.mediaPlayer.getCurrentPosition());
                    txtTimeTvShort.setText(milisecondToTime(String.valueOf(mediaPlayerClass.mediaPlayer.getCurrentPosition())));

                    if (mediaPlayerClass.mediaPlayer.isPlaying()) {
                        btnPlayPauseShort.setBackgroundResource(R.drawable.pause_btn);
                        layoutShortMusicPlay.setVisibility(View.VISIBLE);
                        rotation+=2;
                        rotation_logo.setRotation(rotation);

                    } else {
                        btnPlayPauseShort.setBackgroundResource(R.drawable.play_btn);
                        rotation=0;
                        rotation_logo.setRotation(rotation);

                    }
                }
                else
                    layoutShortMusicPlay.setVisibility(View.GONE);

                if(mediaPlayerClass.songNotExist==1){
                    layoutShortMusicPlay.setVisibility(View.GONE);
                }

                new Handler().postDelayed(this,100);
            }

        });
        return view;
    }
    @SuppressLint("Range")
    private void readMusic() {
        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.DATA + " !=0",
                null,
                MediaStore.Audio.Media.TITLE + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                if(name!=null&& uri!=null && duration!=null)
                    if (isMusicFileExists(uri)) {
                        arrayListMusic.add(new modelClassMusic(name, uri.toString(), duration,id));
                    }
            }
            cursor.close();
        }

        adapter = new musicAdapterClass(getContext(), arrayListMusic);
        musicRecyclerView.setAdapter(adapter);
    }

//    private void runOnUiThread(Runnable runnable) {
//        if (getActivity() != null) {
//            getActivity().runOnUiThread(runnable);
//        }
//    }
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

    private boolean isMusicFileExists(Uri uri) {
        String[] projection = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        boolean exists = false;

        if (cursor != null) {
            exists = cursor.moveToFirst();
            cursor.close();
        }

        return exists;
    }

    public void filterData(String query) {
       if(arrayListMusic.size()!=0){
           if(query!=null){
               ArrayList<modelClassMusic> filteredList = new ArrayList<>();

               if (query.isEmpty()) {
                   filteredList.addAll(arrayListMusic);
               } else {
                   for (modelClassMusic item : arrayListMusic) {
                       if (item.name.toLowerCase().contains(query.toLowerCase())) {
                           filteredList.add(item);
                       }
                   }
               }

               adapter.arrayList = filteredList;
               adapter.notifyDataSetChanged();
           }
       }
    }


}