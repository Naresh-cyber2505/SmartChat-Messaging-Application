package com.example.smartchat.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.smartchat.Adapters.UsersAdapterTop;
import com.example.smartchat.Models.Message;
import com.example.smartchat.Models.Users;
import com.example.smartchat.Adapters.UsersAdapter;
import com.example.smartchat.R;
import com.example.smartchat.databinding.FragmentChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class ChatFragment extends Fragment {

    FragmentChatBinding binding;
    FirebaseAuth auth;
    ArrayList<Users> list;
    FirebaseFirestore firebaseFirestore;
    UsersAdapter usersAdapter;
    UsersAdapterTop usersAdapterTop;

    FirebaseDatabase database;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false);

        FirebaseMessaging.getInstance()
                .getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String userToken) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("userToken", userToken);
                        firebaseFirestore.collection("USERS")
                                .document(FirebaseAuth.getInstance().getUid())
                                .update(map);
//                        Toast.makeText(getContext(), userToken, Toast.LENGTH_SHORT).show();
                    }
                });

        list = new ArrayList<>();

        usersAdapter = new UsersAdapter(list, getContext());
        binding.recyclerUser.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerUser.setAdapter(usersAdapter);

        usersAdapterTop = new UsersAdapterTop(list, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);

        binding.recyclerUser.showShimmer();


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
                binding.recyclerUser.hideShimmer();
                usersAdapter.notifyDataSetChanged();
            }
        });

        firebaseFirestore.collection("USERS").document(auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        Users users = value.toObject(Users.class);

                        assert users != null;
                        Picasso.get()
                                .load(users.getProfileImage())
                                .placeholder(R.drawable.ic_user)
                                .into(binding.profileImage);
                    }
                });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        String currentId = auth.getUid();
        database.getReference().child("live_user").child(currentId).setValue("Online");
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        String currentId = auth.getUid();
        database.getReference().child("live_user").child(currentId).setValue("Offline");
    }
}