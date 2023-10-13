package com.example.restoria_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class easymode extends AppCompatActivity {


    private MediaPlayer mediaPlayer;

    private ImageView easy_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullscreen();
        MediaPlayer mediaPlayer = media.getMediaPlayer(this);
        mediaPlayer.start();
        setContentView(R.layout.activity_easymode);


      //  easy_back = findViewById(R.id.easyback);
      //  easy_back.setOnClickListener(new View.OnClickListener() {
        //    @Override
       //     public void onClick(View view) {
        //        Intent intent=new Intent(easymode.this, Homepage.class);
     //           startActivity(intent);
      //      }
      //  });






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