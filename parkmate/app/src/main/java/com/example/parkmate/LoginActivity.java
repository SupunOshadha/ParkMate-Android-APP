package com.example.parkmate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

    public class LoginActivity extends AppCompatActivity {
        ImageView landingBack;
        TextView goRole, resetPsw;
        EditText edUsername, edPassword;
        Button btnLogin;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize variables for each view by using findViewById
        landingBack = findViewById(R.id.imageViewBack);
        goRole = findViewById(R.id.tvSignup);
        edUsername = findViewById(R.id.edUserName);
        edPassword = findViewById(R.id.edUserPassword);
        btnLogin = findViewById(R.id.btnLoginDashboard);
        resetPsw = findViewById(R.id.tvForgotPsw);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the username and password entered by the user
                String username = edUsername.getText().toString();
                String password = edPassword.getText().toString();
                // Create a new instance of the Database class to access login method
                Database db = new Database(getApplicationContext(), "ParkMate", null, 1);
                // Check if either the username or password fields are empty
                if (username.length() == 0 || password.length() == 0) {
                    // Display a toast message if either field is empty
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the username and password match an existing
                    if (db.login(username, password) == 1) {
                        // display a success message and start the DashboardActivity
                        Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                        // Save the entered username in shared preferences
                        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Username", username);
                        // to save data with key and value
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Username and Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageViewBack:
                        startActivity(new Intent(LoginActivity.this, LandingActivity.class));
                        break;
                    case R.id.tvSignup:
                        startActivity(new Intent(LoginActivity.this, RoleActivity.class));
                        break;
                    case R.id.tvForgotPsw:
                        startActivity(new Intent(LoginActivity.this, ResetActivity.class));
                        break;
                }
            }
        };
        landingBack.setOnClickListener(clickListener);
        goRole.setOnClickListener(clickListener);
        resetPsw.setOnClickListener(clickListener);
    }
}
