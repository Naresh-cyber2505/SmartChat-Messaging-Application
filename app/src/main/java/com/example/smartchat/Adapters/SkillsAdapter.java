package com.example.smartchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartchat.Models.Skills;
import com.example.smartchat.R;
import com.example.smartchat.databinding.ItemContainerSkillsBinding;
import com.example.smartchat.databinding.LayoutEditSkillsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.ViewHolder> {

    private List<Skills> skillsList;
    private Context context;

    public SkillsAdapter(List<Skills> skillsList, Context context) {
        this.skillsList = skillsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_container_skills, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Skills skills = skillsList.get(position);

            holder.binding.textTitle.setText(skills.getSkill());

    }

    @Override
    public int getItemCount() {
        return skillsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemContainerSkillsBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemContainerSkillsBinding.bind(itemView);
        }
    }
}
