package com.example.restoria_app;

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
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://restoria-e00ae-default-rtdb.asia-southeast1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullscreen();
        MediaPlayer mediaPlayer = media.getMediaPlayer(this);
        setContentView(R.layout.activity_leaderboard);



        leaderboard_back = findViewById(R.id.back_to_start);
        leaderboard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(leaderboard.this, Start.class);
                startActivity(intent);
            }
        });


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Assuming you have a TextView instance for easy scoring
        TextView easyScoringTextView = findViewById(R.id.easy_scoring);

        // Check if the user is logged in before accessing their data
        if (currentUser != null) {
            DatabaseReference currentUserRef = databaseReference.child("users").child(currentUser.getUid());

            // Attach a listener to retrieve the highest score
            currentUserRef.child("score").child("allscores").child("highestScore").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get the highest score value from the dataSnapshot
                    Long highestScore = dataSnapshot.getValue(Long.class);

                    if (highestScore != null) {
                        // Update the TextView with the highest score
                        easyScoringTextView.setText("EASY MODE SCORE: " + highestScore);
                    } else {
                        // Handle the case when the highest score is null
                        easyScoringTextView.setText("EASY MODE SCORE: N/A");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle database error
                }
            });
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