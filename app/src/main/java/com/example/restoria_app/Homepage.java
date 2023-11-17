package com.example.restoria_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Homepage extends AppCompatActivity {

     ImageView buttonnew;
     TextView goeasy,gomedium, gohard;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullscreen();

        setContentView(R.layout.activity_homepage);
        MediaPlayer mediaPlayer = media.getMediaPlayer(this);


        buttonnew = findViewById(R.id.backtostart);
        buttonnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Homepage.this, Start.class);
                startActivity(intent);
            }
        });

        goeasy = findViewById(R.id.easy_modes);
        goeasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Homepage.this, video_1.class);
                startActivity(intent);
            }
        });




        gomedium = findViewById(R.id.medium_modes);
        gomedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Homepage.this, video_2.class);
                startActivity(intent);
            }
        });

        gohard = findViewById(R.id.hard_modes);

        gohard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Homepage.this, video_3.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setFullscreen();
        }
    }
    private void setFullscreen() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Homepage.this, Start.class);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Resume the mediaPlayer using your media class
        media.resumeMediaPlayer();
    }
    @Override
    protected void onPause() {
        super.onPause();
        media.pauseMediaPlayer();

    }
}