package com.example.smartchat.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.smartchat.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    private String emailPattern =  "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        binding.forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassWordActivity.class);
                startActivity(intent);
            }
        });

        binding.notHaveAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });


        binding.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, WellComeScreen.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(binding.email.getText())){
            if(!TextUtils.isEmpty(binding.password.getText())){
                binding.SignInBtn.setEnabled(true);
                binding.SignInBtn.setTextColor(Color.rgb(255,255,255));
            }else {
                binding.SignInBtn.setEnabled(false);
                binding.SignInBtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else {
            binding.SignInBtn.setEnabled(false);
            binding.SignInBtn.setTextColor(Color.argb(50,255,255,255));
        }
    }

    private void checkEmailAndPassword(){
        if (binding.email.getText().toString().matches(emailPattern)){
            if (binding.password.length() >= 8){

                binding.progressBar2.setVisibility(View.VISIBLE);
                binding.SignInBtn.setEnabled(false);
                binding.SignInBtn.setTextColor(Color.argb(50,255,255,255));

                auth.signInWithEmailAndPassword(binding.email.getText().toString(),binding.password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                        Intent intent = new Intent(SignInActivity.this, SplashActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    binding.progressBar2.setVisibility(View.INVISIBLE);
                                    binding.SignInBtn.setEnabled(true);
                                    binding.SignInBtn.setTextColor(Color.rgb(255,255,255));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(SignInActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else {
                Toast.makeText(SignInActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(SignInActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
        }
    }
}