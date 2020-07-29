package com.aditya.chatit;

import android.content.Context;
import android.content.Intent;
import android.service.autofill.Dataset;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.aditya.chatit.Model.Chat;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<ChatitUser> mUsers;
    private boolean ischat;

    String theLastMessage;
    public UserAdapter(Context mContext, List<ChatitUser> mUsers, boolean ischat)
    {
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ChatitUser user = mUsers.get(position);
        holder.username.setText(user.getUsername());

        //to check weather the user is verified
        if(user.getVerification().equals("verified"))
        {
            holder.verified_user.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.verified_user.setVisibility(View.GONE);
        }

        //to check weather the user has a profile picture
        if (user.getImageURL().equals("default"))
        {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        else
        {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }

        //to check weather the user is online
        if(ischat){
            if(user.getStatus().equals("online"))
            {
                holder.image_on.setVisibility(View.VISIBLE);
                holder.image_off.setVisibility(View.GONE);
            }
            else
            {
                holder.image_off.setVisibility(View.VISIBLE);
                holder.image_on.setVisibility(View.GONE);

            }
        }
        else {
            holder.image_off.setVisibility(View.GONE);
            holder.image_on.setVisibility(View.GONE);
        }
        //when the user is clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });

        if(ischat){
            lastmessage(user.getId(),holder.last_msg);
        }else {
            holder.last_msg.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image, verified_user;

        private ImageView image_on, image_off;

        private TextView last_msg;


        public ViewHolder(View itemView)
        {
            super(itemView);
            username = itemView.findViewById(R.id.username_user_item);
            profile_image  = itemView.findViewById(R.id.profile_image_user_item);
            verified_user = itemView.findViewById(R.id.verified_user_profile);
            image_off = itemView.findViewById(R.id.img_off);
            image_on = itemView.findViewById(R.id.img_on);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }

    //check for last message
    private void lastmessage(final String user_id, final TextView last_msg)
    {
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) &&chat.getSender().equals(user_id) ||
                    chat.getReceiver().equals(user_id)&& chat.getSender().equals(firebaseUser.getUid())){
                        theLastMessage = chat.getMessage();
                    }
                }
                switch (theLastMessage){
                    case "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
