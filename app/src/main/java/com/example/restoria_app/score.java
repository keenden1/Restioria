package com.example.restoria_app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.text.SimpleDateFormat; // Add this import
import java.util.Date;

public class score extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private TextView totalScoreTextView,doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        totalScoreTextView = findViewById(R.id.scores); // Replace with your TextView's ID
        setFullscreen();
        MediaPlayer mediaPlayer = media.getMediaPlayer(this);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Get a reference to the Firebase Realtime Database
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(currentUser.getUid())
                    .child("score");

            // Retrieve the values for EasyLevel_1 through EasyLevel_5
            ValueEventListener scoreListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        long easyLevel1Score = dataSnapshot.child("EasyLevel_1").getValue(Long.class);
                        long easyLevel2Score = dataSnapshot.child("EasyLevel_2").getValue(Long.class);
                        long easyLevel3Score = dataSnapshot.child("EasyLevel_3").getValue(Long.class);
                        long easyLevel4Score = dataSnapshot.child("EasyLevel_4").getValue(Long.class);
                        long easyLevel5Score = dataSnapshot.child("EasyLevel_5").getValue(Long.class);
                        // Now, retrieve scores for EasyLevel_2 through EasyLevel_5 in a similar manner

                        // Calculate the total score
                        long totalScore = easyLevel1Score + easyLevel2Score + easyLevel3Score + easyLevel4Score + easyLevel5Score;

                        // Display the total score in the TextView
                        totalScoreTextView.setText("Total Score: " + totalScore);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                }
            };

            // Attach the ValueEventListener to the databaseReference
            databaseReference.addValueEventListener(scoreListener);
        }

        doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTotalScoreToAllScores();
                Intent intent = new Intent(score.this, Homepage.class);
                startActivity(intent);
            }
        });
    }

    private void saveTotalScoreToAllScores() {
        if (currentUser != null) {
            // Reference to the 'allscores' node
            DatabaseReference allScoresRef = databaseReference.child("allscores");

            // Query to get all scores
            allScoresRef.orderByChild("score").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long maxScore = 0;

                    // Loop through all scores and find the maximum
                    for (DataSnapshot scoreSnapshot : dataSnapshot.getChildren()) {
                        long scoreValue = scoreSnapshot.child("score").getValue(Long.class);
                        if (scoreValue > maxScore) {
                            maxScore = scoreValue;
                        }
                    }

                    // Now, 'maxScore' contains the highest score among all scores

                    // Create a unique ID for the new score entry
                    String newScoreId = allScoresRef.push().getKey();

                    // Get the total score from the TextView
                    String totalScoreString = totalScoreTextView.getText().toString();
                    long totalScore = Long.parseLong(totalScoreString.replace("Total Score: ", ""));

                    // Create a map to store the score, timestamp, and date
                    Map<String, Object> scoreData = new HashMap<>();
                    scoreData.put("score", totalScore);

                    // Use the current date as the timestamp
                    scoreData.put("timestamp", System.currentTimeMillis());

                    // Add the date as a string (optional)
                    scoreData.put("date", getCurrentDate());

                    // Save the total score to users>currentUser>allscores with the unique ID
                    allScoresRef.child(newScoreId).setValue(scoreData);

                    // You can also update the highest score in the user's main 'score' node
                    updateHighestScore(totalScore);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                }
            });
        }

    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }
    private void updateHighestScore(long totalScore) {
        // Update the highest score in the user's main 'score' node
        databaseReference.child("allscores").child("highestScore").setValue(totalScore);
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(score.this, hardmode.class);
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
