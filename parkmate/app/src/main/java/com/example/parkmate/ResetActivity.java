package com.example.parkmate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetActivity extends AppCompatActivity {
    EditText edUsername, edEmail, edNewPassword, edConfirmPassword;
    Button btnReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        edUsername = findViewById(R.id.edUserNameResetPsw);
        edEmail = findViewById(R.id.edEmailResetPsw);
        edNewPassword = findViewById(R.id.edNewPswResetPsw);
        edConfirmPassword = findViewById(R.id.edConfirmResetPsw);
        btnReset = findViewById(R.id.btn_Reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edUsername.getText().toString();
                String email = edEmail.getText().toString();
                String newPassword = edNewPassword.getText().toString();
                String confirmPassword = edConfirmPassword.getText().toString();

                if (username.isEmpty() || email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(ResetActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ResetActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the user or owner exists in the database
                    Database db = new Database(getApplicationContext(), "ParkMate", null, 1);
                    int result = db.resetPassword(username, email);
                    if (result == 1) {
                        // Update the password in the database
                        db.updatePassword(username, email, newPassword);
                        Toast.makeText(ResetActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    } else {
                        Toast.makeText(ResetActivity.this, "Username or email not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
