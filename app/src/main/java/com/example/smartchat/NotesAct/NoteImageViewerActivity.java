package com.example.smartchat.NotesAct;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.smartchat.Activities.Common;
import com.example.smartchat.R;
import com.example.smartchat.databinding.ActivityNoteImageViewerBinding;

public class NoteImageViewerActivity extends AppCompatActivity {

    ActivityNoteImageViewerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteImageViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String textTitle = getIntent().getStringExtra("textTitle");
        String textUrl = getIntent().getStringExtra("textUrl");
        String textDateTime = getIntent().getStringExtra("textDateTime");

        binding.noteTitle.setText(textTitle);

        binding.textUrl.setText(textUrl);

        binding.date.setText(textDateTime);

        binding.imageView.setImageBitmap(Common.IMAGE_BITMAP);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}