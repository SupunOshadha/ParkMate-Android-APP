package com.example.parkmate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PayActivity extends AppCompatActivity {
    private TextView tv_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        tv_amount = findViewById(R.id.tv_amount);
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("Username", "");
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        float fee = dbHelper.getBookingFee(username);
        // Display the fee in the tv_amount TextView
        tv_amount.setText(String.valueOf(fee));

    }
}
