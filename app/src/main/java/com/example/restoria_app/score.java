package com.example.restoria_app;

import android.content.Intent;
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
import java.util.Map;

public class score extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private TextView totalScoreTextView,doneButton;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        totalScoreTextView = findViewById(R.id.scores); // Replace with your TextView's ID

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
            // Create a unique ID for the new score entry
            String newScoreId = databaseReference.push().getKey();

            // Get the total score from the TextView
            String totalScoreString = totalScoreTextView.getText().toString();
            long totalScore = Long.parseLong(totalScoreString.replace("Total Score: ", ""));

            // Create a map to store the score and timestamp
            Map<String, Object> scoreData = new HashMap<>();
            scoreData.put("score", totalScore);
            scoreData.put("timestamp", ServerValue.TIMESTAMP); // You can use ServerValue to get the server's timestamp

            // Save the total score to users>currentUser>allscores with the unique ID
            databaseReference.child("allscores").child(newScoreId).setValue(scoreData);
        }
    }

}
