package com.example.parkmate;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
public class BookingsActivity extends AppCompatActivity {
    Button btnCheckout, btnCancel;
    TextView tvLotName, tvLotNo, tvDate, tvcheckInTime, tvFee, tvcheckOutTime;
    ImageView btnHome;
    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        btnCheckout = findViewById(R.id.btnCheckOut);
        btnCancel = findViewById(R.id.btnCancel);
        btnHome = findViewById(R.id.imgHome);
        tvLotName = findViewById(R.id.tv_lotName_show);
        tvLotNo = findViewById(R.id.tv_lotNo_show);
        tvDate = findViewById(R.id.tv_date_show);
        tvcheckInTime = findViewById(R.id.tv_check_in_time);
        tvcheckOutTime = findViewById(R.id.tv_check_out_time);
        tvFee = findViewById(R.id.tv_feeDtls);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingsActivity.this,DashboardActivity.class));
            }
        });
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingsActivity.this, PayActivity.class));
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelConfirmationDialog();
            }
        });
        loadBookingDetails();
    }
    private void showCancelConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Reservation");
        builder.setMessage("Are you sure you want to cancel the reservation?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelReservation();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, dismiss the dialog
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void cancelReservation() {
        // Delete the booking details for the current user
        DatabaseHelper dbHelper = new DatabaseHelper(BookingsActivity.this);
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("Username", "");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("bookings", "Username = ?", new String[]{username});
        db.close();
        clearTextViews();
        // Show a toast message to indicate successful cancellation
        Toast.makeText(BookingsActivity.this, "Booking Cancelled", Toast.LENGTH_SHORT).show();
    }
    private void loadBookingDetails() {
        // Retrieve the booking details from the database for the current user
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("Username", "");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "bookings",
                new String[]{"lotName", "lotNo", "date", "checkInTime", "checkOutTime", "fee"},
                "Username = ?",
                new String[]{username},
                null,
                null,
                null
        );
        // Check if the cursor has any rows
        if (cursor.moveToFirst()) {
            // Retrieve the column indices
            int lotNameIndex = cursor.getColumnIndex("lotName");
            int lotNoIndex = cursor.getColumnIndex("lotNo");
            int dateIndex = cursor.getColumnIndex("date");
            int checkInTimeIndex = cursor.getColumnIndex("checkInTime");
            int checkOutTimeIndex = cursor.getColumnIndex("checkOutTime");
            int feeIndex = cursor.getColumnIndex("fee");
            // Check if the column indices are valid
            if (lotNameIndex >= 0 && lotNoIndex >= 0 && dateIndex >= 0 && checkInTimeIndex >= 0 && checkOutTimeIndex >= 0 && feeIndex >= 0) {
                // Retrieve the values from the cursor
                String lotName = cursor.getString(lotNameIndex);
                String lotNo = cursor.getString(lotNoIndex);
                String date = cursor.getString(dateIndex);
                String checkInTime = cursor.getString(checkInTimeIndex);
                String checkOutTime = cursor.getString(checkOutTimeIndex);
                float fee = cursor.getFloat(feeIndex);
                // Set the values to the textviews
                tvLotName.setText(lotName);
                tvLotNo.setText(lotNo);
                tvDate.setText(date);
                tvcheckInTime.setText(checkInTime);
                tvcheckOutTime.setText(checkOutTime);
                tvFee.setText(String.valueOf(fee));
            }
        }
        // Close the cursor and database connection
        cursor.close();
        db.close();
    }
    private void clearTextViews() {
        tvLotName.setText("");
        tvLotNo.setText("");
        tvDate.setText("");
        tvcheckInTime.setText("");
        tvcheckOutTime.setText("");
        tvFee.setText("");
    }
}
