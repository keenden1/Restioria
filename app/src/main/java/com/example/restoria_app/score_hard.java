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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
public class score_hard extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private TextView totalScoreTextView_hard,doneButton_hard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_hard);
        setFullscreen();
        MediaPlayer mediaPlayer = media.getMediaPlayer(this);

        totalScoreTextView_hard = findViewById(R.id.scores_hard); // Replace with your TextView's ID

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
                        long hardLevel1Score = dataSnapshot.child("HardLevel_1").getValue(Long.class);
                        long hardLevel2Score = dataSnapshot.child("HardLevel_2").getValue(Long.class);
                        long hardLevel3Score = dataSnapshot.child("HardLevel_3").getValue(Long.class);
                        long hardLevel4Score = dataSnapshot.child("HardLevel_4").getValue(Long.class);
                        long hardLevel5Score = dataSnapshot.child("HardLevel_5").getValue(Long.class);
                        // Now, retrieve scores for EasyLevel_2 through EasyLevel_5 in a similar manner

                        // Calculate the total score
                        long totalScore = hardLevel1Score + hardLevel2Score + hardLevel3Score + hardLevel4Score + hardLevel5Score;

                        // Display the total score in the TextView
                        totalScoreTextView_hard.setText(String.valueOf(totalScore));
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

        doneButton_hard = findViewById(R.id.done_button_hard);
        doneButton_hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTotalScoreToAllScores();
                highestscoreeasy();
                Intent intent = new Intent(score_hard.this, Homepage.class);
                startActivity(intent);
            }
        });








    }
    private void saveTotalScoreToAllScores() {
        if (currentUser != null) {
            // Reference to the counter node
            DatabaseReference counterRef = FirebaseDatabase.getInstance().getReference("counters").child("scoreCounter");

            // Increment the counter and get the new value
            counterRef.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Long currentCount = mutableData.getValue(Long.class);
                    if (currentCount == null) {
                        currentCount = 1L;
                    } else {
                        currentCount = currentCount + 1;
                    }
                    mutableData.setValue(currentCount);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                    if (committed) {
                        // The counter has been incremented, use the new value as your auto-incremented key
                        Long newScoreId = dataSnapshot.getValue(Long.class);

                        // Get the total score from the TextView
                        String totalScoreString = totalScoreTextView_hard.getText().toString();
                        long totalScore = Long.parseLong(totalScoreString.replace("Total Score: ", ""));

                        // Create a map to store the score and timestamp
                        Map<String, Object> scoreData = new HashMap<>();
                        scoreData.put("score_hard", totalScore);

                        // Save the total score to users>currentUser>allscores with the unique ID
                        databaseReference.child("allscores_hard").child(String.valueOf(newScoreId)).setValue(scoreData);
                    } else {
                        // Handle the error
                    }
                }
            });
        }
    }

    private void highestscoreeasy() {
        if (currentUser != null) {
            DatabaseReference allScoresRef = FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(currentUser.getUid())
                    .child("score")
                    .child("allscores_hard");

            // Use a query to get the scores in descending order and limit to the first result
            Query query = allScoresRef.orderByChild("score_hard").limitToLast(1);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Get the highest score
                            long highestScore = snapshot.child("score_hard").getValue(Long.class);

                            // Update the highest score in the user's main 'score' node
                            DatabaseReference userScoreRef = FirebaseDatabase.getInstance().getReference()
                                    .child("users")
                                    .child(currentUser.getUid())
                                    .child("score");

                            userScoreRef.child("highestscore_hard").setValue(highestScore);
                        }
                    }
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
        databaseReference.child("allscores").child("highestScoreHard").setValue(totalScore);
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(score_hard.this, hardmode.class);
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