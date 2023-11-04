package com.example.restoria_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.TextView;


public class easymode extends AppCompatActivity {

    TextView play ,gotoquiz;
    ImageView backtoeasy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullscreen();
        setContentView(R.layout.activity_easymode);
        MediaPlayer mediaPlayer = media.getMediaPlayer(this);


        backtoeasy = findViewById(R.id.back_to_easy);

        backtoeasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(easymode.this, Homepage.class);
                startActivity(intent);
            }
        });

        play = findViewById(R.id.play);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(easymode.this, video_1.class);
                startActivity(intent);
            }
        });


        gotoquiz = findViewById(R.id.next_quiz_easy);
        gotoquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new AlertDialog.Builder(easymode.this)
                        .setMessage("Are you done WATCHING? \uD83D\uDE0A\uD83D\uDE0A")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(easymode.this, quiz_easy.class);
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
        Intent intent=new Intent(easymode.this, Homepage.class);
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
        // Resume the mediaPlayer using your media class
        media.resumeMediaPlayer();
    }
    @Override
    protected void onPause() {
        super.onPause();
        media.pauseMediaPlayer();

    }

}