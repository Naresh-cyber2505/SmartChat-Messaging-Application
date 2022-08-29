package com.example.smartchat.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartchat.Adapters.SkillsAdapter;
import com.example.smartchat.Models.Skills;
import com.example.smartchat.Models.Users;
import com.example.smartchat.R;
import com.example.smartchat.databinding.FragmentSkillsBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class SkillsFragment extends Fragment {

    FragmentSkillsBinding binding;
    FirebaseDatabase database;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    private List<Skills> skillsList;
    private SkillsAdapter adapter;

    DatabaseReference databaseReference;

    public SkillsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSkillsBinding.inflate(inflater, container, false);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        skillsList = new ArrayList<>();
        adapter = new SkillsAdapter(skillsList, getContext());
        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycler.setAdapter(adapter);

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textInputLayout.setVisibility(View.VISIBLE);
            }
        });

        binding.textCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textInputLayout.setVisibility(View.GONE);
            }
        });

        binding.textAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.input.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "This Field Can't Be Empty", Toast.LENGTH_SHORT).show();
                }
                String uid = auth.getCurrentUser().getUid();

                DatabaseReference reference = databaseReference.child(uid).child("Skills");

                String input = binding.input.getText().toString();



                String id = reference.push().getKey();

                Skills skills = new Skills(input, id);
                reference.push().setValue(skills).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       Toast.makeText(getContext(), "Your Skill "+input+" is Added", Toast.LENGTH_SHORT).show();
                   }
               });
            }
        });

        String uid = auth.getCurrentUser().getUid();

        DatabaseReference reference = databaseReference.child(uid).child("Skills");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                skillsList.clear();
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Skills skills = dataSnapshot.getValue(Skills.class);
                        skillsList.add(skills);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }





}