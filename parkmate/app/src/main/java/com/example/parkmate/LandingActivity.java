package com.example.parkmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {
    Button btnLogin, btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        btnLogin = findViewById(R.id.btnLoginPage);
        btnSignUp = findViewById(R.id.btnSignupPage);
        // Set a click listener on the "Login" button to start the LoginActivity
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(LandingActivity.this,LoginActivity.class));
            }
        });
        // Set a click listener on the "Sign up" button to start the RoleActivity
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(LandingActivity.this,RoleActivity.class));
            }
        });
    }
}