package com.example.smartchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartchat.Models.Interest;
import com.example.smartchat.Models.Skills;
import com.example.smartchat.R;
import com.example.smartchat.databinding.ItemContainerInterestsBinding;
import com.example.smartchat.databinding.ItemContainerSkillsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder> {

    private List<Interest> interests;
    private Context context;

    public InterestAdapter(List<Interest> interests, Context context) {
        this.interests = interests;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_container_interests, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Interest interest = interests.get(position);

            holder.binding.textTitle.setText(interest.getInterest());
            

    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemContainerInterestsBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemContainerInterestsBinding.bind(itemView);
        }
    }
}
