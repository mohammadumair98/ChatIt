package com.aditya.chatit.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<ChatitUser> mUser;

    //for progressbar
    ProgressBar progressBar;

    //search functionality
    EditText search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        //for progressbar
        progressBar = view.findViewById(R.id.user_fragment_progressbar);
        search = view.findViewById(R.id.search_users);

        recyclerView = view.findViewById(R.id.recycler_view_users);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUser = new ArrayList<>();
        readUsers();
        return view;
    }

    private void readUsers()
    {
        progressBar.setVisibility(View.VISIBLE);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search.getText().toString().equals("")) {


                mUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatitUser chatitUser = snapshot.getValue(ChatitUser.class);
                    if (!chatitUser.getId().equals(firebaseUser.getUid())) {
                        mUser.add(chatitUser);
                    }
                }
                userAdapter = new UserAdapter(getContext(), mUser, false);
                recyclerView.setAdapter(userAdapter);
                progressBar.setVisibility(View.GONE);
            }
        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });

        //for searching users
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    //searching
    private void searchUsers(String s)
    {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    ChatitUser user = snapshot.getValue(ChatitUser.class);
                    if (!user.getId().equals(fuser.getUid())){
                        mUser.add(user);
                    }
                }
                userAdapter = new UserAdapter(getContext(),mUser,false);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}