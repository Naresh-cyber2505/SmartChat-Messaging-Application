package com.example.smartchat.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartchat.Adapters.MessagesAdapter;
import com.example.smartchat.Models.Message;
import com.example.smartchat.R;
import com.example.smartchat.databinding.ActivityChatPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatPageActivity extends AppCompatActivity {

    ActivityChatPageBinding binding;
    MessagesAdapter adapter;
    ArrayList<Message> messages;
    FirebaseAuth auth;

    String senderRoom, receiverRoom;

    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase database;
    FirebaseStorage firebaseStorage;

    ProgressDialog dialog;

    String senderUid;
    String receiverUid;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading image...");
        dialog.setCancelable(false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();



        String name = getIntent().getStringExtra("name");
        String profileImage  = getIntent().getStringExtra("profileImage");
        String userToken  = getIntent().getStringExtra("userToken");

//        Toast.makeText(getApplicationContext(), userToken, Toast.LENGTH_SHORT).show();

        receiverUid = getIntent().getStringExtra("uid");
        senderUid = FirebaseAuth.getInstance().getUid();

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        messages = new ArrayList<>();
        adapter = new MessagesAdapter(this, messages, senderRoom, receiverRoom);

        binding.recyclear.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclear.setAdapter(adapter);

        binding.userName.setText(name);
        Picasso.get()
                .load(profileImage)
                .placeholder(R.drawable.ic_user)
                .into(binding.profileImage);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        database.getReference().child("live_user").child(receiverUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String status = snapshot.getValue(String.class);
                    if (!status.isEmpty()){
                        if (status.equals("Offline")){
                            binding.status.setVisibility(View.GONE);
                        }else {
                            binding.status.setText(status);
                            binding.status.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
            database.getReference()
                    .child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            messages.clear();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                Message message = snapshot1.getValue(Message.class);
//                                message.setMessage(snapshot1.getKey());

                                messages.add(message);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageTxt = binding.messageBox.getText().toString();

                binding.messageBox.setText("");

                Date date = new Date();
                Message message = new Message(messageTxt, senderUid, date.getTime());

                HashMap<String , Object> lastMsgObj = new HashMap<>();
                lastMsgObj.put("lastMsg", message.getMessage());
                lastMsgObj.put("lastMsgTime", date.getTime());

                String randomKey = database.getReference().push().getKey();

                database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);

                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(randomKey)
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .child(randomKey)
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                sendNotification(name, message.getMessage(), userToken);
                            }
                        });
                    }
                });
            }
        });


        binding.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 21);
            }
        });

        final Handler handler = new Handler();

        binding.messageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                database.getReference().child("live_user").child(senderUid).setValue("typing...");
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(userStoppedTyping, 1000);
            }

            Runnable userStoppedTyping = new Runnable() {
                @Override
                public void run() {
                    database.getReference().child("live_user").child(senderUid).setValue("Online");
                }
            };
        });

    }

    void sendNotification(String name, String message, String token){
        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            String url = "https://fcm.googleapis.com/fcm/send"; // the data will go through json format

            JSONObject data = new JSONObject();
            data.put("title", name);
            data.put("body", message);
            JSONObject notificationData = new JSONObject();
            notificationData.put("notification", data);
            notificationData.put("to", token);

            JsonObjectRequest request = new JsonObjectRequest(url, notificationData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getLocalizedMessage() , Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    String key = "Key=AAAA0PgoxZ4:APA91bF9VB4d_WGV0ktOO-qlaOPUlbvIHLWgZjmHPwA4Rxz3Sr606GCpw6r2fo0S2iyGV9csRvsWd1OveRHud-a0fdPMI55SCkqkbiDDzVNY5EFl8MXZvAGZjOIfKKP-uDdk12B6GpuW";
                    map.put("Authorization", key);
                    map.put("Content-Type", "application/json");

                    return map;

                }
            };
            queue.add(request);

        }catch (Exception e) {

        }

    }

    private void checkInputs(){
        if (!TextUtils.isEmpty(binding.messageBox.getText())){
            binding.sendBtn.setEnabled(true);
//            binding.sendBtn.setTextColor(Color.rgb(255,255,255));
        }else {
            binding.sendBtn.setEnabled(false);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 21){
            if(data != null){
                Uri selectedImage = data.getData();
                Calendar calendar = Calendar.getInstance();
                StorageReference reference = firebaseStorage.getReference("chats")
                        .child(calendar.getTimeInMillis() + "");
                dialog.show();
                reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String filePath = uri.toString();


                                    String messageTxt = binding.messageBox.getText().toString();


                                    Date date = new Date();
                                    Message message = new Message(messageTxt, senderUid, date.getTime());
                                    message.setMessage("photo");
                                    message.setImageUrl(filePath);
                                    binding.messageBox.setText("");

                                    String randomKey = database.getReference().push().getKey();

                                    HashMap<String , Object> lastMsgObj = new HashMap<>();
                                    lastMsgObj.put("lastMsg", message.getMessage());
                                    lastMsgObj.put("lastMsgTime", date.getTime());

                                    database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                                    database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);

                                    database.getReference().child("chats")
                                            .child(senderRoom)
                                            .child("messages")
                                            .child(randomKey)
                                            .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            database.getReference().child("chats")
                                                    .child(receiverRoom)
                                                    .child("messages")
                                                    .child(randomKey)
                                                    .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                        }
                                    });

                                    Toast.makeText(getApplicationContext(), filePath, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        }
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