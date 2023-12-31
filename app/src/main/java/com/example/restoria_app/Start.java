package com.example.restoria_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
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

import java.util.Set;

public class Start extends AppCompatActivity {

    ImageView setbutton,leader_board;
    TextView btn1,textView5;
    private MediaPlayer mediaPlayer;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://restoria-e00ae-default-rtdb.asia-southeast1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        setFullscreen();
        mAuth = FirebaseAuth.getInstance();
        MediaPlayer mediaPlayer = media.getMediaPlayer(this);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        textView5 = findViewById(R.id.textView5);
        btn1 = findViewById(R.id.start);

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                databaseReference.child("users").orderByChild("Email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                String username = userSnapshot.child("Username").getValue(String.class);
                                textView5.setText(username+" \uD83D\uDE0A\uD83D\uDE0A\uD83D\uDE0A");
                            }
                        } else {
                            textView5.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        } else {

        }





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
        leader_board = findViewById(R.id.imageView4);
        leader_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    DatabaseReference userScoreRef = FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child(currentUser.getUid())
                            .child("score");

                    // Retrieve the values of highestscore_easy, highestscore_medium, and highestscore_hard
                    userScoreRef.child("highestscore_easy").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                long highestScoreEasy = dataSnapshot.getValue(Long.class);

                                // Retrieve the value of highestscore_medium
                                userScoreRef.child("highestscore_medium").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            long highestScoreMedium = dataSnapshot.getValue(Long.class);

                                            // Retrieve the value of highestscore_hard
                                            userScoreRef.child("highestscore_hard").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        long highestScoreHard = dataSnapshot.getValue(Long.class);

                                                        // Calculate the sum of the three scores
                                                        long maxScore = highestScoreEasy + highestScoreMedium + highestScoreHard;

                                                        // Save the sum as max_score
                                                        userScoreRef.child("max_score").setValue(maxScore);

                                                        // Start the leaderboard activity
                                                        Intent intent = new Intent(Start.this, leaderboard.class);
                                                        startActivity(intent);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    // Handle any errors
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle any errors
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle any errors
                        }
                    });
                }
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