package com.example.restoria_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Set;

public class Start extends AppCompatActivity {

    ImageView setbutton;
    TextView btn1;
    private MediaPlayer mediaPlayer;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        setFullscreen();
        mAuth = FirebaseAuth.getInstance();
        MediaPlayer mediaPlayer = media.getMediaPlayer(this);
        mediaPlayer.start();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        btn1 = findViewById(R.id.start);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    Intent intent = new Intent(Start.this, Homepage.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(Start.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        setbutton = findViewById(R.id.imageView5);
        setbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Start.this, Setting.class);
                startActivity(intent);
            }
        });



    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
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