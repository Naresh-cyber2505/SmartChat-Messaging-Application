package com.example.smartchat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartchat.NotesAct.EditNotesActivity;
import com.example.smartchat.Models.Notes;
import com.example.smartchat.NotesListener;
import com.example.smartchat.R;
import com.example.smartchat.databinding.ItemContainerNoteBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    ArrayList<Notes> list;
    Context context;
    NotesListener notesListener;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    public NotesAdapter(ArrayList<Notes> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_container_note, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notes notes = list.get(position);

        holder.binding.textTitle.setText(notes.getNoteTitle());
        holder.binding.textSubTitle.setText(notes.getNoteSubTitle());
        holder.binding.textDateTime.setText(notes.getTextDateTime());

        String textTitle = holder.binding.textTitle.getText().toString();
        String textsubTitle = holder.binding.textSubTitle.getText().toString();
        String textdate = holder.binding.textDateTime.getText().toString();



//        holder.binding.layoutNote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), CreateNoteActivity.class);
//                v.getContext().startActivity(intent);
//            }
//        });
        holder.binding.popUpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                popupMenu.setGravity(Gravity.END);
                popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        Intent intent = new Intent(v.getContext(), EditNotesActivity.class);
                        intent.putExtra("textTitle", textTitle);
                        intent.putExtra("textSubTitle", textsubTitle);
                        intent.putExtra("textDateTime", textdate);
                        v.getContext().startActivity(intent);
                        return false;
                    }
                });

                popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        firebaseFirestore.collection("notes")
                                .whereEqualTo("noteId", firebaseUser.getUid());

                        Toast.makeText(v.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemContainerNoteBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemContainerNoteBinding.bind(itemView);
        }
    }
}
