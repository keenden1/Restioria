package com.example.restoria_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class leaderboard extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    ImageView leaderboard_back;
    private FirebaseAuth mAuth;
    TextView easy_scoring,medium_scoring,hard_scoring,Total_Scores;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://restoria-e00ae-default-rtdb.asia-southeast1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullscreen();
        MediaPlayer mediaPlayer = media.getMediaPlayer(this);
        setContentView(R.layout.activity_leaderboard);

        easy_scoring = findViewById(R.id.easy_scoring);
        medium_scoring = findViewById(R.id.medium_scoring);
        hard_scoring = findViewById(R.id.hard_scoring);
        Total_Scores= findViewById(R.id.Total_Scores);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        leaderboard_back = findViewById(R.id.back_to_start);
        leaderboard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(leaderboard.this, Start.class);
                startActivity(intent);
            }
        });


            if (currentUser != null) {
                DatabaseReference userScoreRef = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(currentUser.getUid())
                        .child("score");

                userScoreRef.child("highestscore_easy").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Get the value of highestscore_easy
                            long highestScore = dataSnapshot.getValue(Long.class);

                            // Set the value to the TextView
                            easy_scoring.setText("Score EASY: " + String.valueOf(highestScore));
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors
                    }
                });

                userScoreRef.child("highestscore_medium").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Get the value of highestscore_medium
                            long highestScoreMedium = dataSnapshot.getValue(Long.class);

                            // Set the value to the TextView
                            medium_scoring.setText("Score MEDIUM: " + String.valueOf(highestScoreMedium));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors
                    }
                });

                userScoreRef.child("highestscore_hard").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Get the value of highestscore_medium
                            long highestScoreMedium = dataSnapshot.getValue(Long.class);

                            // Set the value to the TextView
                            hard_scoring.setText("Score HARD: " + String.valueOf(highestScoreMedium));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors
                    }
                });

                if (currentUser != null) {
                    DatabaseReference userMAX = FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child(currentUser.getUid())
                            .child("score");
                    userMAX.child("max_score").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Get the value of highestscore_easy
                                long maxScore = dataSnapshot.getValue(Long.class);

                                // Set the value to the TextView
                                Total_Scores.setText("IN GAME SCORE: " + String.valueOf(maxScore));
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle any errors
                        }
                    });

                }
            }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(leaderboard.this, Start.class);
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