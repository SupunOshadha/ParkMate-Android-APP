package com.example.parkmate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class RoleActivity extends AppCompatActivity {
    ImageView landingBack, regDriver,regOwner;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        landingBack = findViewById( R.id.imageBack);
        regDriver = findViewById( R.id.imageViewDriver);
        regOwner = findViewById( R.id.imageViewOwner);
        landingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RoleActivity.this,LandingActivity.class));
            }
        });
        regDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( RoleActivity.this,DriverRegisterActivity.class));
            }
        });
        regOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( RoleActivity.this,LotOwnerRegisterActivity.class));
            }
        });
    }
}