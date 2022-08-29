package com.example.smartchat.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.smartchat.Models.Users;
import com.example.smartchat.R;
import com.example.smartchat.databinding.ActivityPhonePasswordRecoveryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PhonePasswordRecoveryActivity extends AppCompatActivity {

    ActivityPhonePasswordRecoveryBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhonePasswordRecoveryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        binding.countryCodePicker.registerCarrierNumberEditText(binding.phoneBox);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.phoneBox.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "This field can't be empty", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), ForgetPasswordOTPActivity.class);
                    intent.putExtra("phoneNumber", binding.countryCodePicker.getFullNumberWithPlus().replace(" ", ""));
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}