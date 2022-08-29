package com.example.smartchat.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.WindowManager;

import com.example.smartchat.Activities.MainActivity;
import com.example.smartchat.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            Thread thread = new Thread() {

                public void run() {
                    try {
                        sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }
            };
            thread.start();
        }
        else {
            Intent intent = new Intent(SplashActivity.this, WellComeScreen.class);
            startActivity(intent);
            finishAffinity();
        }

    }
}