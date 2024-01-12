package com.example.multimediamanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private SparseArray<Fragment> fragmentMap = new SparseArray<>();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragment;
    SearchView searchView;
    BottomNavigationView bottomNavigationView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.botomNavigationView);
        searchView=findViewById(R.id.searchView);


        // Add and show the default fragment when the app starts
        if (savedInstanceState == null) {
            MenuItem defaultMenuItem = bottomNavigationView.getMenu().findItem(R.id.menu_item_audio);
            atachFragment(defaultMenuItem);
        }



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                atachFragment(item);
                return true;
            }
        });

    };
    public void atachFragment( MenuItem item) {
        // Check if the fragment exists in the HashMap
         fragment = fragmentMap.get(item.getItemId());
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchView.setIconified(true);

        // If the fragment doesn't exist, create it and add it to the HashMap
        if (fragment == null) {
            int item_id = item.getItemId();
            if (item_id == R.id.menu_item_videos) {
                fragment = new videoFragmentClass();
            } else if (item_id == R.id.menu_item_audio) {
                fragment = new musicFragmentClass();
            }
            fragmentMap.put(item.getItemId(), fragment);
        }

        // Show the fragment using a FragmentTransaction
        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                musicFragmentClass a;
                videoFragmentClass b;
                if(bottomNavigationView.getSelectedItemId()==R.id.menu_item_audio) {
                    a = (musicFragmentClass) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                    if (a != null) {
                        a.filterData(newText);
                    }
                }else{
                    b = (videoFragmentClass) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                    if (b != null)
                        b.filterData(newText);
                }
                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {
        // Show the confirmation dialog
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.exit)
                .setTitle("Exit")
                .setMessage("Do you want to exit app?")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // If the user clicks "Yes", exit the app
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // If the user clicks "No", do nothing and dismiss the dialog
                        dialog.dismiss();
                    }
                })
                .show();
    }

}