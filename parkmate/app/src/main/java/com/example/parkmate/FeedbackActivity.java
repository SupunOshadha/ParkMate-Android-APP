package com.example.parkmate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FeedbackActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, descriptionEditText;
    private Button submitButton;
    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        // Initialize views
        nameEditText = findViewById(R.id.editTextName);
        emailEditText = findViewById(R.id.editTextEmail);
        descriptionEditText = findViewById(R.id.editTextDescription);
        submitButton = findViewById(R.id.btnSubmit);
        databaseHelper = new DatabaseHelper(this);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values from the EditText fields
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                // Validate input values
                if (name.isEmpty() || email.isEmpty() || description.isEmpty()) {
                    Toast.makeText(FeedbackActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Store feedback details in the database
                    databaseHelper.storeFeedbackDetails(name, email, description, FeedbackActivity.this);
                    Toast.makeText(FeedbackActivity.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                    // Clear input fields
                    nameEditText.setText("");
                    emailEditText.setText("");
                    descriptionEditText.setText("");
                    startActivity(new Intent(FeedbackActivity.this,DashboardActivity.class));
                }
            }
        });
    }
}
