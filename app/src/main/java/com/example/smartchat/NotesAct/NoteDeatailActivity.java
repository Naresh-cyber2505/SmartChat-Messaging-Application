package com.example.smartchat.NotesAct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.example.smartchat.Activities.Common;
import com.example.smartchat.Activities.ProfileMainActivity;
import com.example.smartchat.Activities.ViewImageActivity;
import com.example.smartchat.R;
import com.example.smartchat.databinding.ActivityNoteDeatailBinding;
import com.squareup.picasso.Picasso;

public class NoteDeatailActivity extends AppCompatActivity {

    ActivityNoteDeatailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteDeatailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String textTitle = getIntent().getStringExtra("textTitle");
        String textSubTitle = getIntent().getStringExtra("textSubTitle");
        String textDateTime = getIntent().getStringExtra("textDateTime");
        String textInput = getIntent().getStringExtra("textInput");
        String noteId = getIntent().getStringExtra("noteId");
        String textUrl = getIntent().getStringExtra("textUrl");
        String imageNote = getIntent().getStringExtra("imageNote");

        binding.inputNoteTitle.setText(textTitle);
        binding.inputNoteTitle.setEnabled(false);
        binding.inputNoteSubTitle.setText(textSubTitle);
        binding.inputNoteSubTitle.setEnabled(false);
        binding.inputNote.setText(textInput);
        binding.inputNote.setEnabled(false);
        binding.textDateTime.setText(textDateTime);

        binding.iamgeNote.setVisibility(View.VISIBLE);
        Picasso.get()
                .load(imageNote)
                .into(binding.iamgeNote);

        binding.textWebURL.setText(textUrl);

        binding.iamgeNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.iamgeNote.invalidate();
                Drawable dr = binding.iamgeNote.getDrawable();
                Common.IMAGE_BITMAP = ((BitmapDrawable) dr.getCurrent()).getBitmap();
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(NoteDeatailActivity.this, binding.iamgeNote, "image");
                Intent intent = new Intent(NoteDeatailActivity.this, NoteImageViewerActivity.class);
                intent.putExtra("textTitle", textTitle);
                intent.putExtra("textUrl", textUrl);
                intent.putExtra("textDateTime", textDateTime);
                startActivity(intent, activityOptionsCompat.toBundle());
            }
        });

        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}