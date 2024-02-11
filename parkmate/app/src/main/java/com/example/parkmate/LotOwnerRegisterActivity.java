package com.example.parkmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LotOwnerRegisterActivity extends AppCompatActivity {
    ImageView roleBack;
    TextView existAccount;
    EditText regOwnerUser, regOwnerEmail, regOwnerPassword, regOwnerConfirm;
    Button btnRegOwner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot_owner_register);
        roleBack = findViewById( R.id.imageViewBckRoleOwner);
        existAccount = findViewById( R.id.tvExistAccountOwner);
        regOwnerUser = findViewById( R.id.editTextRegOwnerUsername);
        regOwnerEmail = findViewById( R.id.editTextRegOwnerEmail);
        regOwnerPassword = findViewById( R.id.editTextRegOwnerPassword);
        regOwnerConfirm = findViewById( R.id.editTextRegOwnerConfirmPassword);
        btnRegOwner = findViewById( R.id.btnRegiserOwner);
        btnRegOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values for username, email, password, and confirmation password
                String  ownerUser = regOwnerUser.getText().toString();
                String  ownerEmail = regOwnerEmail.getText().toString();
                String  ownerPassword = regOwnerPassword.getText().toString();
                String  ownerConfirm = regOwnerConfirm.getText().toString();

                Database db = new Database(getApplicationContext(), "ParkMate", null,1);
                // Check if all input fields are filled
                if (ownerUser.length()==0 || ownerEmail.length()==0 || ownerPassword.length()==0 || ownerConfirm.length()==0) {
                    // Show a toast message if any field is empty
                    Toast.makeText(getApplicationContext(), "All fields are required",Toast.LENGTH_SHORT).show();
                }else {
                    // Check if password and confirmation password match
                    if (ownerPassword.compareTo(ownerConfirm)==0){
                        // Check if password meets complexity requirements
                        if(isValid(ownerPassword)){
                            // Register the owner in the database
                            db.ownerRegister(ownerUser,ownerEmail,ownerPassword);
                            // Show a toast message to confirm successful registration
                            Toast.makeText(getApplicationContext(), "Registration Success !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LotOwnerRegisterActivity.this,LotDetailsActivity.class));
                        }else{
                            Toast.makeText(getApplicationContext(), "Password must contain at least 8 characters and having" +
                                            " letter,digit and special character",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Password and Confirm password are not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        roleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( LotOwnerRegisterActivity.this,RoleActivity.class));
            }
        });
        existAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( LotOwnerRegisterActivity.this,LoginActivity.class));
            }
        });
    }
    public static boolean isValid(String password){
        int f1 = 0, f2 = 0, f3 = 0;
        // Check if the password has at least 8 characters
        if(password.length() < 8){
            return false;
        }else {
            // Check if the password contains at least one letter
            for (int p = 0; p<password.length(); p++){
                if(Character.isLetter(password.charAt(p))){
                    f1 = 1;
                }
            }
            // Check if the password contains at least one digit
            for (int q = 0; q<password.length(); q++) {
                if (Character.isDigit(password.charAt(q))) {
                    f2 = 1;
                }
            }
            // Check if the password contains any special characters except spaces
            for (int s = 0; s<password.length(); s++) {
                char c = password.charAt(s);
                if (!(Character.isLetterOrDigit(c) || c == ' ' )) {
                    f3 = 1;
                }
            }
            // If all criteria are met, the password is considered valid
            if(f1==1 && f2==1 && f3==1 ){
                return true;
            }else {
                return  false;
            }
        }
    }
}