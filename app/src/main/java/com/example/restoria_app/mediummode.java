package com.example.restoria_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class mediummode extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private ImageView medium_back;
    private ImageView play_medium ,gotoquizmedium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullscreen();
        MediaPlayer mediaPlayer = media.getMediaPlayer(this);
        setContentView(R.layout.activity_mediummode);

        medium_back = findViewById(R.id.medium_back);
        medium_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mediummode.this, Homepage.class);
                startActivity(intent);
            }
        });

        play_medium = findViewById(R.id.play_medium);

        play_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mediummode.this, video_2.class);
                startActivity(intent);
            }
        });


        gotoquizmedium = findViewById(R.id.next_medium_easy);
        gotoquizmedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mediummode.this)
                        .setMessage("Are you done WATCHING? \uD83D\uDE0A\uD83D\uDE0A")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mediummode.this, quiz_medium.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });






    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(mediummode.this, Homepage.class);
        startActivity(intent);
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
    protected void onResume() {
        super.onResume();
        media.resumeMediaPlayer();
    }
    @Override
    protected void onPause() {
        super.onPause();
        media.pauseMediaPlayer();

    }
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }
}