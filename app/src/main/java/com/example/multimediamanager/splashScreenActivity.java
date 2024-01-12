package com.example.multimediamanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class splashScreenActivity extends AppCompatActivity {
    int api=32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        checkpermissions_();

    }

    private void checkpermissions_() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            api=33;
            if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_MEDIA_AUDIO )==PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_MEDIA_VIDEO )==PackageManager.PERMISSION_GRANTED)
                startActivity();
            else
                requestPermission();
        }
        else{
            if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE )==PackageManager.PERMISSION_GRANTED)
                startActivity();

            else
                requestPermission();
        }
    }

    void startActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(  new Intent(splashScreenActivity.this,MainActivity.class));
                finish();
            }
        },2000);
    }

    void requestPermission(){
        new AlertDialog.Builder(this)
                .setMessage("Give permission to Acess music and video?")
                .setIcon(R.drawable.info)
                .setTitle("permission Required")
                .setPositiveButton("Give Permission", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(api==32)
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},444);
                        else if (api==33) {
                            requestPermissions(new String[]{Manifest.permission.READ_MEDIA_AUDIO,Manifest.permission.READ_MEDIA_VIDEO},555);

                        }

                        dialog.dismiss();
                    }

                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==444){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                checkpermissions_();
            }else {
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
                checkpermissions_();
            }

        }
        if(requestCode==555){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                checkpermissions_();
            }else {
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
                checkpermissions_();
            }

        }
    }
}