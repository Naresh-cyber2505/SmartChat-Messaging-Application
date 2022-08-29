package com.example.smartchat.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.smartchat.NotesAct.CreateNoteActivity;
import com.example.smartchat.NotesAct.EditNotesActivity;
import com.example.smartchat.NotesAct.NoteDeatailActivity;
import com.example.smartchat.Adapters.NotesAdapter;
import com.example.smartchat.Models.Notes;
import com.example.smartchat.R;
import com.example.smartchat.databinding.FragmentNotesBinding;
import com.example.smartchat.databinding.ItemContainerNoteBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;


public class NotesFragment extends Fragment {

    FragmentNotesBinding binding;
    ArrayList<Notes> list;
    NotesAdapter notesAdapter;
    FirebaseDatabase database;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    FirestoreRecyclerAdapter<Notes, NoteViewHolder> noteadapters;
    String docId;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding = FragmentNotesBinding.inflate(inflater, container, false);


       Query query = firebaseFirestore.collection("my_notes")
               .document(auth.getCurrentUser().getUid())
               .collection("notes");

       FirestoreRecyclerOptions<Notes> allNotes = new FirestoreRecyclerOptions.Builder<Notes>()
               .setQuery(query, Notes.class)
               .build();

       noteadapters = new FirestoreRecyclerAdapter<Notes, NoteViewHolder>(allNotes) {
           @Override
           protected void onBindViewHolder(@NonNull NoteViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Notes model) {
               holder.binding.textTitle.setText(model.getNoteTitle());
               holder.binding.textSubTitle.setText(model.getNoteSubTitle());
               holder.binding.textDateTime.setText(model.getTextDateTime());
               holder.binding.textInput.setText(model.getInputNote());
               holder.binding.textUrl.setText(model.getTextURL());

               holder.binding.imageNote.setVisibility(View.VISIBLE);
               Picasso.get()
                       .load(model.getNoteImage())
                       .into(holder.binding.imageNote);


               String textTitle = holder.binding.textTitle.getText().toString();
               String textsubTitle = holder.binding.textSubTitle.getText().toString();
               String textdate = holder.binding.textDateTime.getText().toString();
               String textInput = holder.binding.textInput.getText().toString();
               String textUrl = holder.binding.textUrl.getText().toString();
               String imageNote = holder.binding.imageNote.toString();


               holder.binding.layoutNote.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent = new Intent(v.getContext(), NoteDeatailActivity.class);
                       intent.putExtra("textTitle", textTitle);
                       intent.putExtra("textSubTitle", textsubTitle);
                       intent.putExtra("textDateTime", textdate);
                       intent.putExtra("textInput", textInput);
                       intent.putExtra("noteId", docId);
                       intent.putExtra("textUrl", textUrl);
                       intent.putExtra("imageNote", model.getNoteImage());
                       v.getContext().startActivity(intent);
                   }
               });
               holder.binding.popUpMenu.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       docId = noteadapters.getSnapshots().getSnapshot(position).getId();

                       PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                       popupMenu.setGravity(Gravity.END);
                       popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                           @Override
                           public boolean onMenuItemClick(MenuItem item) {

                               Intent intent = new Intent(v.getContext(), EditNotesActivity.class);
                               intent.putExtra("textTitle", textTitle);
                               intent.putExtra("textSubTitle", textsubTitle);
                               intent.putExtra("textDateTime", textdate);
                               intent.putExtra("textInput", textInput);
                               intent.putExtra("noteId", docId);
                               intent.putExtra("imageNote", model.getNoteImage());
                               intent.putExtra("webUrl", textUrl);
                               v.getContext().startActivity(intent);
                               return false;
                           }
                       });

                       popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                           @Override
                           public boolean onMenuItemClick(MenuItem item) {

                               DocumentReference documentReference = firebaseFirestore.collection("my_notes")
                               .document(auth.getCurrentUser().getUid()).collection("notes")
                                       .document(docId);

                               documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       Toast.makeText(v.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(v.getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                   }
                               });
                               return false;
                           }
                       });
                       popupMenu.show();
                   }
               });
           }

           @NonNull
           @Override
           public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_note, parent, false);
               return new NoteViewHolder(view);
           }
       };

        binding.notesRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        binding.notesRecyclerview.setAdapter(noteadapters);


       binding.floatingButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getContext(), CreateNoteActivity.class);
               intent.putExtra("docId", docId);
               startActivity(intent);
           }
       });


       // search notes

        binding.searchNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.searchNotes.getText().toString() == "")
                {

                }
                else
                {
                    binding.notesRecyclerview.setVisibility(View.VISIBLE);

                    retrieveUsers();
                    searchUser(s.toString().toUpperCase());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       return binding.getRoot();
    }

    private void searchUser(String input) {

        Query documentReference = firebaseFirestore
                .collection("my_notes").document(FirebaseAuth.getInstance().getUid())
                .collection("notes")
                .orderBy("noteTitle")
                .startAt(input)
                .endAt(input + "\uf8ff");


//        documentReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                list.clear();
//
//                for (DocumentSnapshot documentSnapshot : value.getDocuments()){
//
//                }
//
//
//            }
//        });



    }

    private void retrieveUsers() {
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder{

        ItemContainerNoteBinding binding;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemContainerNoteBinding.bind(itemView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        noteadapters.startListening();
    }
}