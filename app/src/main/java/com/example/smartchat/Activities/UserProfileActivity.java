package com.example.smartchat.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.smartchat.R;
import com.example.smartchat.databinding.ActivityUserProfileBinding;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    ActivityUserProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("name");
        String profileImage = getIntent().getStringExtra("profileImage");
        String coverImage  = getIntent().getStringExtra("coverImage");
        String profession  = getIntent().getStringExtra("profession");
        String about  = getIntent().getStringExtra("about");
        String course  = getIntent().getStringExtra("course");
        String skill  = getIntent().getStringExtra("skill");

        binding.userName.setText(name);
        binding.professionB.setText(profession);
        binding.about.setText(about);
        binding.Course.setText(course);


        Picasso.get()
                .load(profileImage)
                .placeholder(R.drawable.ic_user)
                .into(binding.profileImage);

        Picasso.get()
                .load(coverImage)
                .placeholder(R.drawable.placehold)
                .into(binding.coverImage);
    }
}