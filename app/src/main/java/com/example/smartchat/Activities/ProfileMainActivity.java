package com.example.smartchat.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.smartchat.Models.Notes;
import com.example.smartchat.Models.Users;
import com.example.smartchat.R;
import com.example.smartchat.Registration.SignInActivity;
import com.example.smartchat.Registration.WellComeScreen;
import com.example.smartchat.databinding.ActivityProfileMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class ProfileMainActivity extends AppCompatActivity {

    ActivityProfileMainBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    FirebaseStorage storage;

    private final int REQUEST_CODE_COVER = 1;
    private final int REQUEST_CODE_PROFILE = 2;

    private AlertDialog dialogAddURL;
    private AlertDialog dialogAddURL1;
    private AlertDialog dialogAddURL2;
    private AlertDialog dialogAddURL3;
    private AlertDialog dialogAddURL4;

    private String selectedNoteColor;

    private Notes alreadyAvailableNote;
    // for theme
    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor;
    private  int checkedItem;
    private String selected;

    private final String CHECKEDITEM = "checked_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();


        binding.goToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.profileImage.invalidate();
                Drawable dr = binding.profileImage.getDrawable();
                Common.IMAGE_BITMAP = ((BitmapDrawable) dr.getCurrent()).getBitmap();
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ProfileMainActivity.this, binding.profileImage, "image");
                Intent intent = new Intent(ProfileMainActivity.this, ViewImageActivity.class);
                startActivity(intent, activityOptionsCompat.toBundle());
            }
        });



        firebaseFirestore.collection("USERS").document(auth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Users users = documentSnapshot.toObject(Users.class);

                binding.userName.setText(users.getEmail());
                binding.professioTop.setText(users.getProfession());

                binding.userName1.setText(users.getFullName());
                binding.profesion.setText(users.getProfession());
                binding.email.setText(users.getEmail());
                binding.mobile.setText(users.getMobileNumber());
                binding.course.setText(users.getCourse());

                Picasso.get()
                        .load(users.getProfileImage())
                        .placeholder(R.drawable.ic_user)
                        .into(binding.profileImage);
            }
        });

        selectedNoteColor = "#333333";
        initMiscellaneous();

        if(getIntent().getBooleanExtra("isViewOrUpdate", false)){
            alreadyAvailableNote = (Notes) getIntent().getSerializableExtra("note");
//            setViewOrUpdateNote();
        }
    }

    private void initMiscellaneous(){
        final LinearLayout layoutMiscellenoues = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellenoues);
        layoutMiscellenoues.findViewById(R.id.layoutMiscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        final ImageView imageColor1 = layoutMiscellenoues.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscellenoues.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMiscellenoues.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscellenoues.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMiscellenoues.findViewById(R.id.imageColor5);

        layoutMiscellenoues.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#333333";
                imageColor1.setImageResource(R.drawable.ic_done_24);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        layoutMiscellenoues.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FDBE3B";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_done_24);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        layoutMiscellenoues.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done_24);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        layoutMiscellenoues.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#3A52FC";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_done_24);
                imageColor5.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        layoutMiscellenoues.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#000000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_done_24);
                setSubtitleIndicator();
            }
        });
        if (alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && !alreadyAvailableNote.getColor().trim().isEmpty()){
            switch (alreadyAvailableNote.getColor()){
                case "#FDBE3B" :
                    layoutMiscellenoues.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#FF4842" :
                    layoutMiscellenoues.findViewById(R.id.viewColor3).performClick();
                    break;
                case "#3A52FC" :
                    layoutMiscellenoues.findViewById(R.id.viewColor4).performClick();
                    break;
                case "#000000" :
                    layoutMiscellenoues.findViewById(R.id.viewColor5).performClick();
                    break;

            }
        }

        layoutMiscellenoues.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                selectImage();
            }
        });

        layoutMiscellenoues.findViewById(R.id.layoutName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showURLDialog();
            }
        });

        layoutMiscellenoues.findViewById(R.id.layoutProfession).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showProUrl();
            }
        });

        layoutMiscellenoues.findViewById(R.id.layoutPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMainActivity.this, MakeSelectionActivity.class);
                startActivity(intent);
            }
        });

        layoutMiscellenoues.findViewById(R.id.layoutLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showLogoutDia();
            }
        });

        layoutMiscellenoues.findViewById(R.id.layoutCourse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showCourseDia();
            }
        });
        layoutMiscellenoues.findViewById(R.id.layoutAbout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showCourseAbout();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 81);
        finish();
    }

    private void setSubtitleIndicator(){
        GradientDrawable gradientDrawable = (GradientDrawable) binding.viewSubTitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }

    private void showURLDialog() {
        if (dialogAddURL1 == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileMainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_edit_name,
                    (ViewGroup) findViewById(R.id.layoutAddNameContainer)
            );
            builder.setView(view);

            String uid = auth.getCurrentUser().getUid();

            dialogAddURL1 = builder.create();
            if (dialogAddURL1.getWindow() != null){
                dialogAddURL1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputURL = view.findViewById(R.id.inputName);
            inputURL.requestFocus();

            firebaseFirestore.collection("USERS").document(uid)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Users users = documentSnapshot.toObject(Users.class);

                    inputURL.setText(users.getFullName());
                }
            });

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputURL.getText().toString().trim().isEmpty()){
                        Toast.makeText(ProfileMainActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                    }else {

                        String inputN = inputURL.getText().toString();
                        DocumentReference documentReference = firebaseFirestore.collection("USERS")
                                .document(uid);
                        documentReference.update("fullName", inputN).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                startActivity(new Intent(ProfileMainActivity.this, ProfileMainActivity.class));
                                Toast.makeText(ProfileMainActivity.this, "Your Name is updated to " + " "+inputN, Toast.LENGTH_SHORT).show();
                                dialogAddURL1.dismiss();
                            }
                        });
                    }
                }
            });

            view.findViewById(R.id.textCancal).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddURL1.dismiss();
                }
            });
        }
        dialogAddURL1.show();
    }

    private void  showProUrl(){
        if (dialogAddURL == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileMainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_edit_profession,
                    (ViewGroup) findViewById(R.id.layoutAddProContainer)
            );
            builder.setView(view);

            String uid = auth.getCurrentUser().getUid();


            dialogAddURL = builder.create();
            if (dialogAddURL.getWindow() != null){
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputPro = view.findViewById(R.id.inputPro);
            inputPro.requestFocus();

            firebaseFirestore.collection("USERS").document(uid)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Users users = documentSnapshot.toObject(Users.class);

                    inputPro.setText(users.getProfession());
                }
            });

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputPro.getText().toString().trim().isEmpty()){
                        Toast.makeText(ProfileMainActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                    }else {

                        String inputN = inputPro.getText().toString();
                        DocumentReference documentReference = firebaseFirestore.collection("USERS")
                                .document(uid);
                        documentReference.update("profession", inputN).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(ProfileMainActivity.this, "Your profession is updated "+inputN, Toast.LENGTH_SHORT).show();
                                dialogAddURL.dismiss();
                            }
                        });
                    }
                }
            });

            view.findViewById(R.id.textCancal).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddURL.dismiss();
                }
            });
        }
        dialogAddURL.show();
    }

    private void  showLogoutDia(){

        if (dialogAddURL2 == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileMainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_logout,
                    (ViewGroup) findViewById(R.id.layoutLogoutContainer)
            );
            builder.setView(view);

            dialogAddURL2 = builder.create();
            if (dialogAddURL2.getWindow() != null){
                dialogAddURL2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth.signOut();
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            view.findViewById(R.id.textCancal).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddURL2.dismiss();
                }
            });
        }
        dialogAddURL2.show();
    }

    private void  showCourseDia(){
        if (dialogAddURL3 == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileMainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_edit_course,
                    (ViewGroup) findViewById(R.id.layoutCourseContainer)
            );
            builder.setView(view);

            String uid = auth.getCurrentUser().getUid();


            dialogAddURL3 = builder.create();
            if (dialogAddURL3.getWindow() != null){
                dialogAddURL3.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputPro = view.findViewById(R.id.inputCourse);
            inputPro.requestFocus();

            firebaseFirestore.collection("USERS").document(uid)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Users users = documentSnapshot.toObject(Users.class);

                    inputPro.setText(users.getCourse());
                }
            });

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputPro.getText().toString().trim().isEmpty()){
                        Toast.makeText(ProfileMainActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                    }else {

                        String inputN = inputPro.getText().toString();
                        DocumentReference documentReference = firebaseFirestore.collection("USERS")
                                .document(uid);
                        documentReference.update("course", inputN).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(ProfileMainActivity.this, "Your Course is updated "+inputN, Toast.LENGTH_SHORT).show();
                                dialogAddURL3.dismiss();
                            }
                        });
                    }
                }
            });

            view.findViewById(R.id.textCancal).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddURL3.dismiss();
                }
            });
        }
        dialogAddURL3.show();
    }

    private void  showCourseAbout(){
        if (dialogAddURL4 == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileMainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_edit_about,
                    (ViewGroup) findViewById(R.id.layoutAboutContainer)
            );
            builder.setView(view);

            String uid = auth.getCurrentUser().getUid();


            dialogAddURL4 = builder.create();
            if (dialogAddURL4.getWindow() != null){
                dialogAddURL4.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputPro = view.findViewById(R.id.inputAbout);

            inputPro.requestFocus();

            firebaseFirestore.collection("USERS").document(uid)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Users users = documentSnapshot.toObject(Users.class);
                        inputPro.setText(users.getAbout());
                }
            });

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputPro.getText().toString().trim().isEmpty()){
                        Toast.makeText(ProfileMainActivity.this, "Enter Something", Toast.LENGTH_SHORT).show();
                    }else {

                        String inputN = inputPro.getText().toString();
                        DocumentReference documentReference = firebaseFirestore.collection("USERS")
                                .document(uid);
                        documentReference.update("about", inputN).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(ProfileMainActivity.this, "Your About is updated "+inputN, Toast.LENGTH_SHORT).show();
                                dialogAddURL4.dismiss();
                            }
                        });
                    }
                }
            });

            view.findViewById(R.id.textCancal).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddURL4.dismiss();
                }
            });
        }
        dialogAddURL4.show();
    }

}