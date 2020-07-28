package com.aditya.chatit.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aditya.chatit.ChatitUser;
import com.aditya.chatit.Model.Chat;
import com.aditya.chatit.R;
import com.aditya.chatit.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<ChatitUser> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container,false);
        recyclerView = view.findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        userList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               userList.clear();
               for (DataSnapshot snapshot: dataSnapshot.getChildren())
               {
                   Chat chat = snapshot.getValue(Chat.class);

                   if(chat.getSender().equals(fuser.getUid()))
                   {
                       userList.add(chat.getReceiver());
                   }

                   if (chat.getReceiver().equals(fuser.getUid()))
                   {
                       userList.add(chat.getSender());
                   }
               }
               readchats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    public void readchats(){
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    ChatitUser user = snapshot.getValue(ChatitUser.class);

                    //display 1 user from chats
                    for(String id : userList) {
                        if(user.getId().equals(id)) {
                            if(mUsers.size() != 0) {
                                for (ChatitUser user1 : mUsers) {
                                    if(!user.getId().equals(user1.getId())){
                                        mUsers.add(user);
                                    }
                                }
                            }
                            else {
                                mUsers.add(user);
                            }
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}