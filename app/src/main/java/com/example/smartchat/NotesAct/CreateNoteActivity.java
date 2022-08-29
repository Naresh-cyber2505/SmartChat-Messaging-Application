package com.example.smartchat.NotesAct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.smartchat.Fragment.NotesFragment;
import com.example.smartchat.Models.Notes;
import com.example.smartchat.R;
import com.example.smartchat.databinding.ActivityCreateNoteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CreateNoteActivity extends AppCompatActivity {

    ActivityCreateNoteBinding binding;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;


    private String selectedNoteColor;
    private String selectedImagePath;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;


    Uri selectImageUri;

    private AlertDialog dialogAddURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        String textTitle = getIntent().getStringExtra("textTitle");
        String docId = getIntent().getStringExtra("docId");
        binding.inputNoteTitle.setText(textTitle);


        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );

//        binding.imageSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveNote();
//            }
//        });

        binding.imageRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.iamgeNote.setImageBitmap(null);
                binding.iamgeNote.setVisibility(View.GONE);
                binding.imageRemoveImage.setVisibility(View.GONE);
                selectedImagePath = "";
            }
        });

        selectedImagePath = "";
        initMiscellaneous();

        binding.imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.inputNoteTitle.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Note title can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (binding.inputNoteSubTitle.getText().toString().isEmpty()
                        && binding.inputNote.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Note title can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectImageUri != null){
                    Calendar calendar = Calendar.getInstance();
                    StorageReference reference = storage.getReference().child("notes_images")
                            .child(auth.getCurrentUser().getUid()).child(calendar.getTimeInMillis() + "");
                    reference.putFile(selectImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String fileLink = uri.toString();

                                        String noteTitle = binding.inputNoteTitle.getText().toString().trim();
                                        String noteSubTitle = binding.inputNoteSubTitle.getText().toString().trim();
                                        String inputNote = binding.inputNote.getText().toString().trim();

                                        String dateTime = binding.textDateTime.getText().toString().trim();

                                        String url = binding.textWebURL.getText().toString().trim();


                                        String uid = auth.getCurrentUser().getUid();
                                        Notes notes = new Notes(noteTitle, noteSubTitle, inputNote, dateTime, uid, url, fileLink);

                                        DocumentReference documentReference = firebaseFirestore
                                                .collection("my_notes").document(uid).collection("notes")
                                                .document();

                                        documentReference.set(notes).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getApplicationContext(), "Data added", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), NotesFragment.class));
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }else{

                    String noteTitle = binding.inputNoteTitle.getText().toString().trim();
                    String noteSubTitle = binding.inputNoteSubTitle.getText().toString().trim();
                    String inputNote = binding.inputNote.getText().toString().trim();

                    String dateTime = binding.textDateTime.getText().toString().trim();

                    String url = binding.textWebURL.getText().toString().trim();


                    String uid = auth.getCurrentUser().getUid();
                    Notes notes = new Notes(noteTitle, noteSubTitle, inputNote, dateTime, uid, url, "noImage");

                    DocumentReference documentReference = firebaseFirestore
                            .collection("my_notes").document(uid).collection("notes")
                            .document();

                    documentReference.set(notes).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Data added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), NotesFragment.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    private void initMiscellaneous() {
        final LinearLayout layoutMiscellenoues = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellenoues);
        layoutMiscellenoues.findViewById(R.id.layoutMiscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
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


        layoutMiscellenoues.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                selectImage();
            }
        });

        layoutMiscellenoues.findViewById(R.id.layoutAddUri).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showURLDialog();
            }
        });

    }


    private void showURLDialog() {
        if (dialogAddURL == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_add_url,
                    (ViewGroup) findViewById(R.id.layoutAddUrlContainer)
            );
            builder.setView(view);

            dialogAddURL = builder.create();
            if (dialogAddURL.getWindow() != null) {
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputURL = view.findViewById(R.id.inputName);
            inputURL.requestFocus();

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputURL.getText().toString().trim().isEmpty()) {
                        Toast.makeText(CreateNoteActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();
                    } else if (!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()) {
                        Toast.makeText(CreateNoteActivity.this, "Enter valid URL", Toast.LENGTH_SHORT).show();
                    } else {
                        String url = inputURL.getText().toString().trim();
                        binding.textWebURL.setText(url);
                        binding.layoutWebURL.setVisibility(View.VISIBLE);
                        dialogAddURL.dismiss();

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

    private void selectImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 81);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            selectImageUri = data.getData();
            binding.iamgeNote.setImageURI(selectImageUri);

                if (selectImageUri != null){
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        binding.iamgeNote.setImageBitmap(bitmap);
                        binding.iamgeNote.setVisibility(View.VISIBLE);
                        binding.imageRemoveImage.setVisibility(View.VISIBLE);

                    }catch (Exception exception){
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }


    private void setSubtitleIndicator() {
        GradientDrawable gradientDrawable = (GradientDrawable) binding.viewSubTitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }
}