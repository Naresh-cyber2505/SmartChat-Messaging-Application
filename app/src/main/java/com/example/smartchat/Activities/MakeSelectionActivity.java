package com.example.smartchat.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.smartchat.Registration.ForgotPassWordActivity;
import com.example.smartchat.databinding.ActivityMakeSelectionBinding;

public class MakeSelectionActivity extends AppCompatActivity {

    ActivityMakeSelectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPassWordActivity.class));
            }
        });
    }
}