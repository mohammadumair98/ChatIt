package com.aditya.chatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Signup_activity extends AppCompatActivity {

    TextInputEditText  username, email, password;
    Button register_the_user;
    FirebaseAuth mfirebaseauth;
    DatabaseReference reference;

    //extra button
    TextView have_acc_already;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);

        username = findViewById(R.id. user_id_signup_user);
        email = findViewById(R.id.email_signup_user);
        password = findViewById(R.id.password_signup_user);
        register_the_user = findViewById(R.id.signup_screen_signup_btn);
        mfirebaseauth = FirebaseAuth.getInstance();

        //extra buttons
        have_acc_already = findViewById(R.id.signup_already_have_an_account);
        have_acc_already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create_acc = new Intent( Signup_activity.this,login_activity.class);
                startActivity(create_acc);
                finish();
            }
        });

        //when the user clicks on the SignUp button , the process of Creating the user with the FirebaseAuth takes place here
        register_the_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_user_name = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if(TextUtils.isEmpty(txt_user_name)|| TextUtils.isEmpty(txt_email)|| TextUtils.isEmpty(txt_password))
                {
                    //if any field is empty
                    Toast.makeText(Signup_activity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                }
                else if(txt_password.length()< 6)
                {
                    //If password is less than 6 characters
                    Toast.makeText(Signup_activity.this, "Password must be more than 6 characters", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //if the user enters all the fields
                    register(txt_user_name, txt_email,txt_password);

                }
            }
        });

    }


    private void register(final String username, String email, String password)
    {
        final LoadingDialog loadingDialog = new LoadingDialog(Signup_activity.this);
        loadingDialog.startLoadDialog();
            mfirebaseauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        FirebaseUser firebaseUser = mfirebaseauth.getCurrentUser();
                        String userid = firebaseUser.getUid();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("username", username);
                        hashMap.put("imageURL", "default");
                        hashMap.put("verification", "unverified");

                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    loadingDialog.dismissDialog();
                                    //on successfull registration of the user
                                    Intent intent = new Intent(Signup_activity.this, home_activity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(Signup_activity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();

                    }
                }
            });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent go_to_main_screen = new Intent(Signup_activity.this, MainActivity.class);
        startActivity(go_to_main_screen);
        finish();
    }
}