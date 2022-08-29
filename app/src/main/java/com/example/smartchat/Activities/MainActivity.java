package com.example.smartchat.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.smartchat.Fragment.ChatFragment;
import com.example.smartchat.Fragment.NotesFragment;
import com.example.smartchat.Fragment.ProfileFragment;
import com.example.smartchat.R;
import com.example.smartchat.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setSupportActionBar(binding.toolbar);
        MainActivity.this.setTitle("My Profile");

        auth = FirebaseAuth.getInstance();

        binding.toolbar.setVisibility(View.GONE);

        FirebaseUser currentUser = auth.getCurrentUser();

        if(savedInstanceState==null){
            binding.chipNav.setItemSelected(R.id.home, true);
            fragmentManager = getSupportFragmentManager();
            ChatFragment homeFragment = new ChatFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, homeFragment)
                    .commit();
        }

        binding.chipNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {


                Fragment fragment = null;
                switch (i){
                    case R.id.home:
                        binding.toolbar.setVisibility(View.GONE);
                        fragment = new ChatFragment();
                        break;

                    case R.id.notificationMenu:
                        binding.toolbar.setVisibility(View.GONE);
                        fragment = new NotesFragment();
                        break;

                    case R.id.profile:
                        binding.toolbar.setVisibility(View.VISIBLE);
                        fragment = new ProfileFragment();
                        break;
                }

                if(fragment!=null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }else {
                    Toast.makeText(MainActivity.this, "error in fragment", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                Intent intent = new Intent(MainActivity.this, ProfileMainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}