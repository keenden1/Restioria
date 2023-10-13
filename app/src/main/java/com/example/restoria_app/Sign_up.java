package com.example.restoria_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class Sign_up extends AppCompatActivity {
    ImageView imgbutton1;
    EditText textemail, textpassword,textpasswordrpt, textusername;

    TextView register;
    private MediaPlayer mediaPlayer;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://restoria-e00ae-default-rtdb.asia-southeast1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullscreen();

        setContentView(R.layout.activity_sign_up);
        MediaPlayer mediaPlayer = media.getMediaPlayer(this);
        mediaPlayer.start();

        textemail = findViewById(R.id.emailaddress2);
        textusername = findViewById(R.id.username);
        textpassword = findViewById(R.id.password1);
        textpasswordrpt = findViewById(R.id.rppassword);
        register = findViewById(R.id.textView8);


        imgbutton1 = findViewById(R.id.imageView9);
        imgbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Sign_up.this, MainActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String emailtxt = String.valueOf(textemail.getText());
                final String passwordtxt = String.valueOf(textpassword.getText());
                final String usernametxt = String.valueOf(textusername.getText());
                final String passwordrpttxt = String.valueOf(textpasswordrpt.getText());



                if (TextUtils.isEmpty(emailtxt) || TextUtils.isEmpty(passwordtxt) || TextUtils.isEmpty(usernametxt) || TextUtils.isEmpty(passwordrpttxt)) {
                    Toast.makeText(Sign_up.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!passwordtxt.equals(passwordrpttxt)) {
                    Toast.makeText(Sign_up.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }


                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(usernametxt)) {
                            Toast.makeText(Sign_up.this, "Username Already Used", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (containsNumber(usernametxt)) {
                                Toast.makeText(Sign_up.this, "Username cannot contain numbers", Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("users").setValue(usernametxt);
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                firebaseAuth.createUserWithEmailAndPassword(emailtxt, passwordtxt)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Sign_up.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signOut();
                                    Intent intent = new Intent(Sign_up.this, Log_in.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Sign_up.this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Sign_up.this, MainActivity.class);
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
    private boolean containsNumber(String text) {
        for (char c : text.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
}