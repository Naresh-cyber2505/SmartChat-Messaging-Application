package com.example.smartchat.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.smartchat.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PhoneNumberActivity extends AppCompatActivity {

    ActivityPhoneNumberBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();



//        String userEnteredPhoneNo = binding.phoneBox.getText().toString();
//        String phoneNo = "+"+binding.countryCodePicker.getFullNumber()+userEnteredPhoneNo;

        binding.countryCodePicker.registerCarrierNumberEditText(binding.phoneBox);

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.phoneBox.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "This field can't be empty", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(PhoneNumberActivity.this, OTPActivity.class);
                    intent.putExtra("phoneNumber", binding.countryCodePicker.getFullNumberWithPlus().replace(" ", ""));
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
