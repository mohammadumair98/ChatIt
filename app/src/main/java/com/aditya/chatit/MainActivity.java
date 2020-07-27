package com.aditya.chatit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button login, signup;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login_screen_btn);
        signup = findViewById(R.id.signup_screen_btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_login_screen = new Intent(MainActivity.this, login_activity.class);
                startActivity(go_to_login_screen);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_sign_up_screen = new Intent(MainActivity.this, Signup_activity.class);
                startActivity(go_to_sign_up_screen);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!= null)
        {
            Intent homelogin = new Intent(getApplicationContext(),home_activity.class);
            startActivity(homelogin);
            finish();
        }
    }
}