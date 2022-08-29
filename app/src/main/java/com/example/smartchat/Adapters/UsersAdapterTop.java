package com.example.smartchat.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartchat.Activities.ChatPageActivity;
import com.example.smartchat.Activities.UserProfileActivity;
import com.example.smartchat.Fragment.ProfileFragment;
import com.example.smartchat.Models.Skills;
import com.example.smartchat.Models.Users;
import com.example.smartchat.R;
import com.example.smartchat.databinding.RowConversationBinding;
import com.example.smartchat.databinding.RowConversationTopBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UsersAdapterTop extends RecyclerView.Adapter<UsersAdapterTop.ViewHolder>{

    ArrayList<Users> list;
    Context context;

    public UsersAdapterTop(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_conversation_top, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = list.get(position);


        Picasso.get()
                .load(users.getProfileImage())
                .placeholder(R.drawable.ic_user)
                .into(holder.binding.profile);

        holder.binding.userName.setText(users.getFullName());

        String senderId = FirebaseAuth.getInstance().getUid();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra("name", users.getFullName());
                intent.putExtra("uid", users.getUid());
                intent.putExtra("profileImage", users.getProfileImage());
                intent.putExtra("coverImage", users.getCoverPhoto());
                intent.putExtra("profession", users.getProfession());
                intent.putExtra("about", users.getAbout());
                intent.putExtra("course", users.getCourse());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RowConversationTopBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowConversationTopBinding.bind(itemView);
        }
    }
}
