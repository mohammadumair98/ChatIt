package com.aditya.chatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaDataSource;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aditya.chatit.Model.Chat;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference;

    Intent intent;

    //for sending messages
    ImageButton btn_send;
    EditText text_send;

    //for displaying messages
    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //for the toolbar design
        Toolbar toolbar = findViewById(R.id.toolbar_message_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.message_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //for the message sending part
        btn_send = findViewById(R.id.btn_message_send);
        text_send = findViewById(R.id.text_send_message);

        profile_image = findViewById(R.id.profile_image_message_activity);
        username = findViewById(R.id.username_message_activity);
        intent = getIntent();
        //to get the users details
        final String userid = intent.getStringExtra("userid");
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ChatitUser user = dataSnapshot.getValue(ChatitUser.class);
                    username.setText(user.getUsername());
                    if(user.getImageURL().equals("default"))
                    {
                        profile_image.setImageResource(R.mipmap.ic_launcher);
                    }
                    else {
                        Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);
                    }
                    readMessages(fuser.getUid(),userid,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        //when the user writes a message and clicks on the send button
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg= text_send.getText().toString();
                if(!msg.equals(""))
                {
                    sendMessage(fuser.getUid(),userid,msg);
                }
                else
                {
                    Toast.makeText(MessageActivity.this, "You cannot send an empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
    }

    //for recyclerview
    public void readMessages(final String myid, final String userid, final String imageurl)
    {
        mchat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid)||
                    chat.getReceiver().equals(userid) && chat.getSender().equals(myid))
                    {
                        mchat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendMessage(String sender, String receiver, String message)
    {
        DatabaseReference reference1  = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference1.child("chats").push().setValue(hashMap);
    }


}