package com.example.smartchat.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.smartchat.R;
import com.example.smartchat.databinding.ActivityViewImageBinding;

public class ViewImageActivity extends AppCompatActivity {

    ActivityViewImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageView.setImageBitmap(Common.IMAGE_BITMAP);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}