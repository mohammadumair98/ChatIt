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

public class login_activity extends AppCompatActivity {

    //this page is for those users who already have an account on chat-it
    TextInputEditText email, password;
    Button login_btn;

    //for invoking firebase authentication
    FirebaseAuth firebaseAuth;

    //Extra buttons textviews
    TextView forgot_password, create_new_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        
        email = findViewById(R.id.email_login_user);
        password = findViewById(R.id.password_login_user);
        login_btn = findViewById(R.id.login_screen_login_btn);

        firebaseAuth = FirebaseAuth.getInstance();

        //other buttons
        forgot_password = findViewById(R.id.login_forgot_password);
        create_new_account = findViewById(R.id.login_screen_do_not_have_account);

        //when the login button is clicked
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // for the custom loading dialog that i have created
                final LoadingDialog loadingDialog = new LoadingDialog(login_activity.this);
                loadingDialog.startLoadDialog();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password))
                {
                    Toast.makeText(login_activity.this, "Both Email and password field must be filled", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
                }
                else
                {
                    firebaseAuth.signInWithEmailAndPassword(txt_email,txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        loadingDialog.dismissDialog();
                                        Intent go_to_home = new Intent(login_activity.this, home_activity.class);
                                        startActivity(go_to_home);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(login_activity.this, "Some error occurred, try again!", Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismissDialog();
                                    }
                                }
                            });
                }
            }
        });

        //in case the user does not have an account
        create_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create_acc = new Intent(login_activity.this, Signup_activity.class);
                startActivity(create_acc);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent go_to_main_screen = new Intent(login_activity.this, MainActivity.class);
        startActivity(go_to_main_screen);
        finish();
    }
}