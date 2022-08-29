package com.example.smartchat.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.smartchat.databinding.ActivitySetNewPasswordBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SetNewPassword extends AppCompatActivity {

    ActivitySetNewPasswordBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetNewPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        String phoneNo = getIntent().getStringExtra("phoneNumber");

        binding.phoneBox.setText(phoneNo);
//        binding.phoneBox.setEnabled(false);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = binding.phoneBox.getText().toString().trim();

//                DocumentReference documentReference = firebaseFirestore.collection("USERS")
//                        .document(auth.getCurrentUser().getUid());
//
//                documentReference.update("mobileNumber", phone).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
//                    }
//                });

//                DocumentReference documentReference = firebaseFirestore.collection("USERS")
//                        .document(uid);
//                documentReference.update("fullName", inputN).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        startActivity(new Intent(ProfileMainActivity.this, ProfileMainActivity.class));
//                        Toast.makeText(ProfileMainActivity.this, "Your Name is updated to " + " "+inputN, Toast.LENGTH_SHORT).show();
//                        dialogAddURL1.dismiss();
//                    }
//                });

            }
        });

    }
}