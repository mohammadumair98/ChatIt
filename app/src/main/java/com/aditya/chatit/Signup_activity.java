package com.aditya.chatit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Signup_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent go_to_main_screen = new Intent(Signup_activity.this, MainActivity.class);
        startActivity(go_to_main_screen);
        finish();
    }
}