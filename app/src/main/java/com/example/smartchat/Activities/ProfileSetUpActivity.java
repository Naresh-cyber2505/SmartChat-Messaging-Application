package com.example.smartchat.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.smartchat.Models.Users;
import com.example.smartchat.R;
import com.example.smartchat.databinding.ActivityProfileSetUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileSetUpActivity extends AppCompatActivity {

    ActivityProfileSetUpBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileSetUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

//        DocumentReference documentReference = firebaseFirestore.collection("USERS").document(auth.getCurrentUser().getUid());
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                Users users = value.toObject(Users.class);
//                binding.userName.setText(users.getFullName());
//
//                Picasso.get()
//                        .load(users.getProfileImage())
//                        .placeholder(R.drawable.ic_user)
//                        .into(binding.profileImage);
//            }
//        });


        binding.flotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 23);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String userId;
        userId = auth.getCurrentUser().getUid();

        if (requestCode == 23){
            if(data.getData() != null){
                Uri uri = data.getData();

                binding.profileImage.setImageURI(uri);


                binding.conBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StorageReference reference = storage.getReference().child("profile_photo")
                                .child(userId);
                        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()){
                                            String uri = task.getResult().toString();
                                            String username = binding.userName.getText().toString();
                                            String Course = binding.course.getText().toString();

                                            DocumentReference reference1 = firebaseFirestore.collection("USERS").document(userId);
                                            reference1.update("profileImage", uri);
                                            reference1.update("fullName", username);
                                            reference1.update("course", Course);

                                            Intent intent = new Intent(ProfileSetUpActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finishAffinity();

                                            Toast.makeText(getApplicationContext(), "Profile is Created Successfully", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }
                        });
                    }
                });



            }
        }
    }
}