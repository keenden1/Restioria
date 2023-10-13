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

public class Sign_up extends AppCompatActivity {
    ImageView imgbutton1;
    EditText textemail, textpassword,textpasswordrpt, username;

    TextView register;
    private MediaPlayer mediaPlayer;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullscreen();

        setContentView(R.layout.activity_sign_up);
        MediaPlayer mediaPlayer = media.getMediaPlayer(this);
        mediaPlayer.start();
        textemail = findViewById(R.id.emailaddress2);
        username = findViewById(R.id.username);
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
                String username,email,password,passwordrpt;
                email = String.valueOf(textemail.getText());
                password = String.valueOf(textpassword.getText());
                username = String.valueOf(textpassword.getText());
                passwordrpt = String.valueOf(textpassword.getText());


                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
                    Toast.makeText(Sign_up.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(passwordrpt)) {
                    Toast.makeText(Sign_up.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Sign_up.this, "Registration Succesful", Toast.LENGTH_SHORT).show();
                                    saveUsernameInDatabase(username);
                                    Intent intent = new Intent(Sign_up.this, Log_in.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Sign_up.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
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
    private void saveUsernameInDatabase(String username) {
        // You can use Firebase Realtime Database or Firestore here to save the username with the user's UID.
        // This would typically involve creating a "users" node where each user has a unique UID and their username.
        // You would set the username for the authenticated user in your database.
        // Here's a simplified example using Firebase Realtime Database (ensure you set up the database in the Firebase Console):
        // DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // if (user != null) {
        //     databaseReference.child(user.getUid()).child("username").setValue(userUsername);
        // }
    }
}