package com.example.smartchat.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.smartchat.databinding.ActivityOtpactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    ActivityOtpactivityBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    String codeBySystem;
    String otpid;
    String phoneNo;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Sending OTP!");
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.show();


        Intent intent = getIntent();
        phoneNo = intent.getStringExtra("phoneNumber");

//        String email = getIntent().getStringExtra("email");
//        String fullName = getIntent().getStringExtra("fullName");
//        String password = getIntent().getStringExtra("password");
//        String conPassword = getIntent().getStringExtra("conPassword");

        binding.phonetext.setText(phoneNo);


//        sendVerificationCodeToUser();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                dialog.dismiss();
                                otpid = s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.pinView.getText().toString().isEmpty())
                    Toast.makeText(OTPActivity.this, "Blank Field can not be processed", Toast.LENGTH_SHORT).show();
                else if (binding.pinView.getText().toString().length() != 6)
                    Toast.makeText(OTPActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                else
                {
                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(otpid, binding.pinView.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           Intent intent = new Intent(OTPActivity.this, PhoneNumberSignUpActivity.class);
                           intent.putExtra("phoneNumber", phoneNo);
                           startActivity(intent);
                           finish();

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(OTPActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                });
    }

}