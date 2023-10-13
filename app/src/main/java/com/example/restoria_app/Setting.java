package com.example.restoria_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Setting extends AppCompatActivity {
    private ImageView button_1;
    private MediaPlayer mediaPlayer;
    private SharedPreferences sharedPreferences;
    private AudioManager audioManager;
    private ImageView unmute;
    private ImageView mute;
    private boolean isMuted; // To keep track of the mute/unmute state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen();
        setContentView(R.layout.activity_setting);

        mediaPlayer = media.getMediaPlayer(this);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        unmute = findViewById(R.id.unmute);
        mute = findViewById(R.id.mute);

        // Retrieve the mute/unmute state from SharedPreferences
        isMuted = sharedPreferences.getBoolean("isMuted", false);
        updateMuteUI();

        unmute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMuted = false;
                updateMuteUI();
                saveMuteState();
            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMuted = true;
                updateMuteUI();
                saveMuteState();
            }
        });

        button_1 = findViewById(R.id.imageView2);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Setting.this, Start.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Setting.this, Start.class);
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

    private void saveMuteState() {
        // Save the mute/unmute state in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isMuted", isMuted);
        editor.apply();
    }

    private void updateMuteUI() {
        if (isMuted) {
            mediaPlayer.setVolume(0.0f, 0.0f);
            mute.setVisibility(View.GONE);
            unmute.setVisibility(View.VISIBLE);
        } else {
            mediaPlayer.setVolume(1.0f, 1.0f);
            unmute.setVisibility(View.GONE);
            mute.setVisibility(View.VISIBLE);
        }
    }
}
