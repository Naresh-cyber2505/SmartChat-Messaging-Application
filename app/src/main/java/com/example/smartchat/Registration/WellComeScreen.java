package com.example.smartchat.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.smartchat.Activities.MainActivity;
import com.example.smartchat.databinding.ActivityWellComeScreenBinding;
import com.google.firebase.auth.FirebaseAuth;

public class WellComeScreen extends AppCompatActivity {

    ActivityWellComeScreenBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWellComeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            Intent intent = new Intent(WellComeScreen.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }

        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WellComeScreen.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WellComeScreen.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        binding.phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WellComeScreen.this,PhoneNumberActivity.class);
                startActivity(intent);
            }
        });


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = auth.getCurrentUser();
//
//        if (currentUser == null){
//            Intent intent = new Intent(WellComeScreen.this, SignUpActivity.class);
//            startActivity(intent);
//            finish();
//        }else {
//            Intent intent2 = new Intent(WellComeScreen.this, MainActivity.class);
//            startActivity(intent2);
//            finish();
//        }
//    }
}