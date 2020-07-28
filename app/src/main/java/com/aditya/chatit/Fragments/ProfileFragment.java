package com.aditya.chatit.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.aditya.chatit.ChatitUser;
import com.aditya.chatit.ForgotPassword_Bottom_dialog;
import com.aditya.chatit.R;
import com.aditya.chatit.home_activity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    CircleImageView imageView;
    TextView username;
    TextView email;

    DatabaseReference reference;
    FirebaseUser fuser;

    //forgot password
    Button forgotpassword;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container,false);
        imageView = view.findViewById(R.id.profile_image_profile);
        username = view.findViewById(R.id.username_profile);
        email = view.findViewById(R.id.email_profile);

        //forgot password
        forgotpassword = view.findViewById(R.id.profile_forgot_password);


        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        email.setText(""+fuser.getEmail());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChatitUser user = dataSnapshot.getValue(ChatitUser.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getContext()).load(user.getImageURL()).into(imageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //when the user clicks on forgot password
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return view;
    }
}