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
import com.example.smartchat.databinding.ActivityPhoneNumberSignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PhoneNumberSignUpActivity extends AppCompatActivity {

    ActivityPhoneNumberSignUpBinding binding;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    Users users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        String phoneNunmber = getIntent().getStringExtra("phoneNumber");

        binding.phoneNumber.setText(phoneNunmber);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneNumberSignUpActivity.this, WellComeScreen.class);
                startActivity(intent);
                finish();
            }
        });

        binding.haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneNumberSignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        String phoneNumber = binding.phoneNumber.getText().toString();

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
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
        binding.phoneNumber.addTextChangedListener(new TextWatcher() {
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

    }

    private void checkEmailAndPassword() {

        Drawable drawable = getResources().getDrawable(R.drawable.ic_twotone_error_outline_24);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        String fullName = binding.username.getText().toString();
        String email = binding.email.getText().toString();
        String phoneNumber = binding.phoneNumber.getText().toString();
        String password = binding.password.getText().toString();
        String conPassword = binding.conPassword.getText().toString();
        String profession = binding.profession.getText().toString();

        if (binding.email.getText().toString().matches(emailPattern)) {
            if (binding.password.getText().toString().equals(binding.conPassword.getText().toString())) {

                binding.progressBar.setVisibility(View.VISIBLE);
                binding.signUpBtn.setEnabled(true);
                binding.signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));

//                    Users users = new Users(fullName,email,phoneNumber,password,conPassword,auth.getUid());

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = task.getResult().getUser().getUid();
                            users = new Users(fullName, email, phoneNumber, password, conPassword, userId, profession);
                            firebaseFirestore.collection("USERS")
                                    .document(userId)
                                    .set(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(PhoneNumberSignUpActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PhoneNumberSignUpActivity.this, SplashActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(PhoneNumberSignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });
            } else {
                binding.conPassword.setError("Password Doesn't Matched", drawable);
            }
        } else {
            binding.email.setError("Invalid Email.!", drawable);
        }
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(binding.email.getText())) {
            if (!TextUtils.isEmpty(binding.username.getText())) {
                if (!TextUtils.isEmpty(binding.password.getText()) && binding.password.length() >= 8) {
                    if (!TextUtils.isEmpty(binding.conPassword.getText())) {
                        if (!TextUtils.isEmpty(binding.phoneNumber.getText()) && binding.phoneNumber.length() == 13) {
                            if (!TextUtils.isEmpty(binding.profession.getText())) {
                                binding.signUpBtn.setEnabled(true);
                                binding.signUpBtn.setTextColor(Color.rgb(255, 255, 255));
                            } else {
                                binding.signUpBtn.setEnabled(false);
                                binding.signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
                            }
                        } else {
                            binding.signUpBtn.setEnabled(false);
                            binding.signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
                        }
                    } else {
                        binding.signUpBtn.setEnabled(false);
                        binding.signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
                    }
                } else {
                    binding.signUpBtn.setEnabled(false);
                    binding.signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
                }
            } else {
                binding.signUpBtn.setEnabled(false);
                binding.signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            binding.signUpBtn.setEnabled(false);
            binding.signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }
}