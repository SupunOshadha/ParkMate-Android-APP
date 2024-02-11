package com.example.parkmate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DriverRegisterActivity extends AppCompatActivity {
    ImageView roleBack;
    TextView existAccount;
    EditText edUsername,edEmail, edPassword, edConfirm;
    Button btnReg;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);
        roleBack = findViewById( R.id.imageViewBckRole);
        existAccount = findViewById( R.id.tvExistAccount);
        edUsername = findViewById( R.id.editTextRegUsername);
        edEmail = findViewById( R.id.editTextRegEmail);
        edPassword = findViewById( R.id.editTextRegPassword);
        edConfirm = findViewById( R.id.editTextRegConfirmPassword);
        btnReg = findViewById( R.id.btnRegiser);
        roleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( DriverRegisterActivity.this,RoleActivity.class));
            }
        });
        existAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( DriverRegisterActivity.this,LoginActivity.class));
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting values entered by user
                String userName = edUsername.getText().toString();
                String userEmail = edEmail.getText().toString();
                String userPassword = edPassword.getText().toString();
                String userConfirm = edConfirm.getText().toString();
                // Creating Database object to perform database operations
                Database db = new Database(getApplicationContext(), "ParkMate", null,1);
                // Checking if all fields are filled
                if (userName.length()==0 || userEmail.length()==0 || userPassword.length()==0 || userConfirm.length()==0) {
                    Toast.makeText(getApplicationContext(), "All fields are required",Toast.LENGTH_SHORT).show();
                }else {
                    // Checking if password and confirm password match
                    if (userPassword.compareTo(userConfirm)==0){
                        // Checking if password is valid or not
                        if(isValid(userPassword)){
                            // Registering user in database
                            db.register(userName,userEmail,userPassword);
                            Toast.makeText(getApplicationContext(), "Registration Success !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DriverRegisterActivity.this,LoginActivity.class));
                        }else{
                            Toast.makeText(getApplicationContext(), "Password must contain at least 8 characters and having letter," +
                                    "digit and special character", Toast.LENGTH_SHORT).show();
                        }
                }else {
                        Toast.makeText(getApplicationContext(), "Password and Confirm password are not match",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    // validate password
    public static boolean isValid(String password){
        int f1 = 0, f2 = 0, f3 = 0;
        if(password.length() < 8){
            return false;
        }else {
            for (int p = 0; p<password.length(); p++){
                if(Character.isLetter(password.charAt(p))){
                    f1 = 1;
                }
            }
            for (int q = 0; q<password.length(); q++) {
                if (Character.isDigit(password.charAt(q))) {
                    f2 = 1;
                }
            }
            for (int s = 0; s<password.length(); s++) {
                char c = password.charAt(s);
                if (!(Character.isLetterOrDigit(c) || c == ' ' )) {
                    f3 = 1;
                }
            }
            if(f1==1 && f2==1 && f3==1 ){
                return true;
            }else {
                return  false;
            }
        }
    }
}
