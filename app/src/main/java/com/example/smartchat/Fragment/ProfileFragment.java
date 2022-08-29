package com.example.smartchat.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.smartchat.Activities.ProfileMainActivity;
import com.example.smartchat.Activities.SkillsInActivity;
import com.example.smartchat.Adapters.UsersAdapterTop;
import com.example.smartchat.Models.Message;
import com.example.smartchat.Models.Skills;
import com.example.smartchat.Models.Users;
import com.example.smartchat.R;
import com.example.smartchat.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    FirebaseStorage storage;
    ArrayList<Users> list;
    UsersAdapterTop usersAdapterTop;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;

    FirebaseDatabase database;

    DatabaseReference databaseReference;

    boolean isOpen = false;

    ProgressDialog progressDialog;

    private final int REQUEST_CODE_COVER = 1;
    private final int REQUEST_CODE_PROFILE = 2;

    SharedPreferences sharedPreferences = null;

    private BottomSheetDialog bottomSheetDialog;

    String uid;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        initActionClick();

        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forword);
        rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backword);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating your profile...");

        list = new ArrayList<>();

        usersAdapterTop = new UsersAdapterTop(list, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.recyclerUser2.setLayoutManager(layoutManager);
        binding.recyclerUser2.setAdapter(usersAdapterTop);


        try {
             uid = auth.getCurrentUser().getUid();
        }catch (Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        DatabaseReference reference = databaseReference.child(uid).child("Skills");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Skills skills = dataSnapshot.getValue(Skills.class);
                        binding.skill.setText(skills.getSkill());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        firebaseFirestore.collection("USERS").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Message message = new Message();

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    Users users = documentSnapshot.toObject(Users.class);
                    if (!users.getUid().equals(FirebaseAuth.getInstance().getUid())){
                        list.add(users);
                    }
                }
                binding.recyclerUser2.hideShimmer();
                usersAdapterTop.notifyDataSetChanged();
            }
        });


        DocumentReference documentReference = firebaseFirestore.collection("USERS").document(uid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                Users users = value.toObject(Users.class);
                assert users != null;
                binding.userName.setText(users.getFullName());
                binding.professionB.setText(users.getProfession());
                binding.about.setText(users.getAbout());

                Picasso.get()
                        .load(users.getCoverPhoto())
                        .placeholder(R.drawable.placehold)
                        .into(binding.coverImage);
                Picasso.get()
                        .load(users.getProfileImage())
                        .placeholder(R.drawable.ic_user)
                        .into(binding.profileImage);

            }
        });

        binding.SkillsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SkillsInActivity.class);
                startActivity(intent);
            }
        });

        binding.InterestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SkillsInActivity.class);
                startActivity(intent);
            }
        });


        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileMainActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    private void initActionClick() {
        binding.floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                showBottomSheetPickPhoto();
            }
        });
    }

    private void showBottomSheetPickPhoto() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dia_gallery, null);

        ((View) view.findViewById(R.id.profilePhoto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profilePicPhoto();

                bottomSheetDialog.dismiss();
            }
        });
        ((View) view.findViewById(R.id.coverPhoto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                coverPicPhoto();

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialog = null;
            }
        });

        bottomSheetDialog.show();
    }

    private void coverPicPhoto() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_COVER);
    }

    private void profilePicPhoto() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PROFILE);
    }

    private void animateFab(){
        if (isOpen){
            binding.floatBtn.startAnimation(rotateForward);
//            binding.textFab1.startAnimation(fabClose);
//            binding.textFab2.startAnimation(fabClose);
//            binding.fab1.startAnimation(fabClose);
//            binding.fab2.startAnimation(fabClose);
//            binding.fab1.setClickable(false);
//            binding.fab2.setClickable(false);
            isOpen=false;
        }else {
            binding.floatBtn.startAnimation(rotateBackward);
//            binding.textFab1.startAnimation(fabOpen);
//            binding.textFab2.startAnimation(fabOpen);
//            binding.fab1.startAnimation(fabOpen);
//            binding.fab2.startAnimation(fabOpen);
//            binding.fab1.setClickable(true);
//            binding.fab2.setClickable(true);
            isOpen=true;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_COVER) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                binding.coverImage.setImageURI(uri);
//            Bitmap bitmap= MediaStore.Images.Media.getBitmap(contentResolver,uri);
                progressDialog.show();
                StorageReference reference = storage.getReference().child("cover_photo")
                        .child(auth.getCurrentUser().getUid());
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    String uid = FirebaseAuth.getInstance().getUid();
                                    String down = task.getResult().toString();

                                    DocumentReference documentReference = firebaseFirestore.collection("USERS").document(uid);
                                    documentReference.update("coverPhoto", down);
                                    Toast.makeText(getContext(), "CoverPhoto Is Changed Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        } else {
            if (requestCode == REQUEST_CODE_PROFILE){
                if (data.getData() != null) {
                    Uri uri = data.getData();

                    binding.profileImage.setImageURI(uri);

                    progressDialog.show();
                    StorageReference storageReference = storage.getReference()
                            .child("profile_photo").child(auth.getCurrentUser().getUid());
                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        String uid = FirebaseAuth.getInstance().getUid();
                                        String down = task.getResult().toString();

                                        DocumentReference documentReference = firebaseFirestore.collection("USERS").document(uid);
                                        documentReference.update("profileImage", down);
                                        Toast.makeText(getContext(), "ProfilePhoto Is Changed Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });
                }
            }

        }
    }
}