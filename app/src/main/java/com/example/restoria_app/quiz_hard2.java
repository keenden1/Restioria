package com.example.restoria_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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

public class quiz_hard2 extends AppCompatActivity {
    private TextView countdownTextView;
    private CountDownTimer countDownTimer;
    private boolean isTimerPaused = false;
    private long timeRemaining;
    private TextView button_hard_1a,button_hard_2a,button_hard_3a,questionhard2;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://restoria-e00ae-default-rtdb.asia-southeast1.firebasedatabase.app");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_hard2);
        setFullscreen();
        MediaPlayer mediaPlayer = media.getMediaPlayer(this);


        countdownTextView = findViewById(R.id.timer);

        if (savedInstanceState != null) {
            isTimerPaused = savedInstanceState.getBoolean("isTimerPaused");
            timeRemaining = savedInstanceState.getLong("timeRemaining");
        }

        // Start or resume the countdown
        if (isTimerPaused) {
            startCountdown((int) (timeRemaining / 1000));
        } else {
            startCountdown(10);
        }

        button_hard_1a= findViewById(R.id.button_hard_1a);
        button_hard_2a= findViewById(R.id.button_hard_2a);
        button_hard_3a= findViewById(R.id.button_hard_3a);
        questionhard2 = findViewById(R.id.question_hard_2);

        int totalQuestions = 5;
        int randomQuestionNumber = (int) (Math.random() * totalQuestions) + 1;
        String randomQuestionPath = "hard/questions2/question" + randomQuestionNumber;


        databaseReference.child(randomQuestionPath).child("QA").addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txt = dataSnapshot.getValue(String.class);
                questionhard2.setText(txt);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child(randomQuestionPath).child("A").addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txt_1 = dataSnapshot.getValue(String.class);
                button_hard_1a.setText(txt_1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child(randomQuestionPath).child("B").addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txt_2 = dataSnapshot.getValue(String.class);
                button_hard_2a.setText(txt_2);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child(randomQuestionPath).child("C").addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txt_3 = dataSnapshot.getValue(String.class);
                button_hard_3a.setText(txt_3);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        button_hard_1a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check the condition in Firebase Realtime Database
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

                // Add a ValueEventListener to listen for changes in the database
                databaseRef.child(randomQuestionPath).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String questionA = dataSnapshot.child("A").getValue(String.class);
                        String expectedAnswer = dataSnapshot.child("answer").getValue(String.class);

                        if (questionA != null && expectedAnswer != null && questionA.equals(expectedAnswer)) {
                            button_hard_1a.setBackgroundResource(R.drawable.border3);
                            button_hard_2a.setEnabled(false);
                            button_hard_3a.setEnabled(false);
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                String userId = currentUser.getUid();
                                DatabaseReference userRef = databaseRef.child("users").child(userId);
                                userRef.child("score").child("HardLevel_2").setValue(1);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(quiz_hard2.this, quiz_hard3.class);
                                    startActivity(intent);
                                }
                            }, 1000);
                        }else{
                            button_hard_1a.setBackgroundResource(R.drawable.border4);
                            button_hard_2a.setEnabled(false);
                            button_hard_3a.setEnabled(false);
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                String userId = currentUser.getUid();
                                DatabaseReference userRef = databaseRef.child("users").child(userId);
                                userRef.child("score").child("HardLevel_2").setValue(0);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(quiz_hard2.this, quiz_hard3.class);
                                    startActivity(intent);
                                }
                            }, 1000);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Handle database error
                    }
                });
            }
        });


        button_hard_2a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check the condition in Firebase Realtime Database
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

                // Add a ValueEventListener to listen for changes in the database
                databaseRef.child(randomQuestionPath).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String questionA = dataSnapshot.child("B").getValue(String.class);
                        String expectedAnswer = dataSnapshot.child("answer").getValue(String.class);

                        if (questionA != null && expectedAnswer != null && questionA.equals(expectedAnswer)) {
                            button_hard_2a.setBackgroundResource(R.drawable.border3);
                            button_hard_1a.setEnabled(false);
                            button_hard_3a.setEnabled(false);
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                String userId = currentUser.getUid();
                                DatabaseReference userRef = databaseRef.child("users").child(userId);
                                userRef.child("score").child("HardLevel_2").setValue(1);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(quiz_hard2.this, quiz_hard3.class);
                                    startActivity(intent);
                                }
                            }, 1000);
                        }else{
                            button_hard_2a.setBackgroundResource(R.drawable.border4);
                            button_hard_1a.setEnabled(false);
                            button_hard_3a.setEnabled(false);
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                String userId = currentUser.getUid();
                                DatabaseReference userRef = databaseRef.child("users").child(userId);
                                userRef.child("score").child("HardLevel_2").setValue(0);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(quiz_hard2.this, quiz_hard3.class);
                                    startActivity(intent);
                                }
                            }, 1000);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Handle database error
                    }
                });
            }
        });

        button_hard_3a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check the condition in Firebase Realtime Database
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

                // Add a ValueEventListener to listen for changes in the database
                databaseRef.child(randomQuestionPath).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String questionA = dataSnapshot.child("C").getValue(String.class);
                        String expectedAnswer = dataSnapshot.child("answer").getValue(String.class);

                        if (questionA != null && expectedAnswer != null && questionA.equals(expectedAnswer)) {
                            button_hard_3a.setBackgroundResource(R.drawable.border3);
                            button_hard_1a.setEnabled(false);
                            button_hard_2a.setEnabled(false);
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                String userId = currentUser.getUid();
                                DatabaseReference userRef = databaseRef.child("users").child(userId);
                                userRef.child("score").child("HardLevel_2").setValue(1);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Start the hardmode.xml activity here
                                    Intent intent = new Intent(quiz_hard2.this, quiz_hard3.class);
                                    startActivity(intent);
                                }
                            }, 1000);
                        }else{
                            button_hard_3a.setBackgroundResource(R.drawable.border4);
                            button_hard_1a.setEnabled(false);
                            button_hard_2a.setEnabled(false);
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                String userId = currentUser.getUid();
                                DatabaseReference userRef = databaseRef.child("users").child(userId);
                                userRef.child("score").child("HardLevel_2").setValue(0);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Start the hardmode.xml activity here
                                    Intent intent = new Intent(quiz_hard2.this, quiz_hard3.class);
                                    startActivity(intent);
                                }
                            }, 1000);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Handle database error
                    }
                });
            }
        });







    }

    private void startCountdown(int seconds) {
        countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                int secondsRemaining = (int) millisUntilFinished / 1000;
                countdownTextView.setText(String.valueOf(secondsRemaining));
            }

            public void onFinish() {
                countdownTextView.setText("0");
                next();
            }
        }.start();
    }
    private void next() {
        Intent intent = new Intent(quiz_hard2.this, quiz_hard3.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
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
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to GIVE UP?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(quiz_hard2.this, hardmode.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        media.resumeMediaPlayer();
        if (isTimerPaused) {
            startCountdown((int) (timeRemaining / 1000));
            isTimerPaused = false;
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        media.pauseMediaPlayer();
        if (countDownTimer != null) {
            // Pause the countdown timer without resetting it
            countDownTimer.cancel();
            isTimerPaused = true;
        }
    }
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isTimerPaused", isTimerPaused);
        outState.putLong("timeRemaining", timeRemaining);
        super.onSaveInstanceState(outState);
    }


}