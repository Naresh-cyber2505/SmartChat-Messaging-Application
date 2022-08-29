package com.example.smartchat.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.smartchat.Activities.MainActivity;
import com.example.smartchat.Models.Users;
import com.example.smartchat.R;
import com.example.smartchat.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private String emailPattern =  "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, WellComeScreen.class);
                startActivity(intent);
                finish();
            }
        });
        binding.haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
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
        binding.username.addTextChangedListener(new TextWatcher() {
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
        binding.conPassword.addTextChangedListener(new TextWatcher() {
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

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });
    }

    private void checkEmailAndPassword() {

        String fullName = binding.username.getText().toString();
        String email = binding.email.getText().toString();
//        String phoneNumber = binding..getText().toString();
        String password = binding.password.getText().toString();
        String conPassword = binding.conPassword.getText().toString();
        String profession = binding.profession.getText().toString();

        // custem error icon
        Drawable drawable = getResources().getDrawable(R.drawable.ic_twotone_error_outline_24);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        if (binding.email.getText().toString().matches(emailPattern)){
            if (binding.password.getText().toString().equals(binding.conPassword.getText().toString())){

                binding.progressBar.setVisibility(View.VISIBLE);
                binding.signUpBtn.setEnabled(false);
                binding.signUpBtn.setTextColor(Color.argb(50,255,255,255));

//                Users users = new Users(fullName,email,password,conPassword,auth.getUid());
                auth.createUserWithEmailAndPassword(binding.email.getText().toString(), binding.password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(getApplicationContext(), "Registration Success", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                                String uid = task.getResult().getUser().getUid();
                                String phoneNo = task.getResult().getUser().getPhoneNumber();
                                users = new Users(fullName,email,phoneNo,password,conPassword,uid, profession);

                                if (task.isSuccessful()){
                                    firebaseFirestore.collection("USERS")
                                            .document(uid)
                                            .set(users)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Intent intent = new Intent(SignUpActivity.this, SplashActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }else {
                                                        binding.progressBar.setVisibility(View.INVISIBLE); // if error accure this will get invisible
                                                        binding.signUpBtn.setEnabled(true);
                                                        binding.signUpBtn.setTextColor(Color.rgb(255,255,255));
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }else {
                                    binding.progressBar.setVisibility(View.INVISIBLE); // if error accure this will get invisible
                                    binding.signUpBtn.setEnabled(true);
                                    binding.signUpBtn.setTextColor(Color.rgb(255,255,255));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else {
                binding.conPassword.setError("Password Doesn't Matched",drawable);
            }
        }else {
            binding.email.setError("Invalid Email.!", drawable);
        }
    }

    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Verification Email is sent, Verify and log in Again", Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Fail to send Verification Email", Toast.LENGTH_SHORT).show();

        }
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(binding.email.getText())){
            if (!TextUtils.isEmpty(binding.username.getText())){
                if (!TextUtils.isEmpty(binding.password.getText()) && binding.password.length() >= 8){
                    if (!TextUtils.isEmpty(binding.conPassword.getText())){
                        binding.signUpBtn.setEnabled(true);
                        binding.signUpBtn.setTextColor(Color.rgb(255,255,255));
                    }else {
                        binding.signUpBtn.setEnabled(false);
                        binding.signUpBtn.setTextColor(Color.argb(50,255,255,255));
                    }
                }else {
                    binding.signUpBtn.setEnabled(false);
                    binding.signUpBtn.setTextColor(Color.argb(50,255,255,255));
                }
            }else {
                binding.signUpBtn.setEnabled(false);
                binding.signUpBtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else {
            binding.signUpBtn.setEnabled(false);
            binding.signUpBtn.setTextColor(Color.argb(50,255,255,255));
        }
    }
}