package com.example.multimediamanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link videoFragmentClass#newInstance} factory method to
 * create an instance of this fragment.
 */
public class videoFragmentClass extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public videoFragmentClass() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment videoFragmentClass.
     */
    // TODO: Rename and change types and number of parameters
    public static videoFragmentClass newInstance(String param1, String param2) {
        videoFragmentClass fragment = new videoFragmentClass();
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
    RecyclerView videoRecyclerView;
    ArrayList<ModelClassVideo> arrayListVideo;
    videoAdapterClass adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_video_class, container, false);
        videoRecyclerView=view.findViewById(R.id.video_recyclerView);
        arrayListVideo=new ArrayList<>();
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            readVideos();
        if(arrayListVideo.size()==0)
            Toast.makeText(getContext(), "No Videos found on your device", Toast.LENGTH_LONG).show();



        // Inflate the layout for this fragment
        return view;
    }

    @SuppressLint("Range")
    public void readVideos() {
        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Video.Media.DATA+"!=0",
                null,
                MediaStore.Video.Media.TITLE+ " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String duration=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                if(name!=null&& uri!=null && duration!=null)
                    arrayListVideo.add(new ModelClassVideo(name,String.valueOf(uri),duration));
            }
            cursor.close();
        }

        adapter = new videoAdapterClass(getContext(),arrayListVideo);
        videoRecyclerView.setAdapter(adapter);
    }

    public void filterData(String query) {
        if(arrayListVideo.size()!=0){
            if(query!=null){
                ArrayList<ModelClassVideo> filteredList = new ArrayList<>();

                if (query.isEmpty()) {
                    filteredList.addAll(arrayListVideo);
                } else {
                    for (ModelClassVideo item : arrayListVideo) {
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